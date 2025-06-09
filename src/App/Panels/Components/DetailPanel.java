package App.Panels.Components;

import App.Callbacks.RunnableCallback;
import App.Callbacks.ShapeIntersectCallback;
import App.Models.Guild.GuildMember;
import App.Models.Mission.Mission;
import App.Models.Mission.MissionAssignment;
import App.Models.Mission.MissionStatus;
import App.Panels.GuiUtil.ImagePanel;
import App.Panels.GuiUtil.RoundedPanel;
import App.StaticUtils.ColorUtils;
import App.StaticUtils.FontUtils;
import App.Types.Coords;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.time.Duration;

public class DetailPanel extends RoundedPanel implements Runnable {
    private final JPanel thisPanel;
    private final JLabel statusLabel;
    private Thread timerThread;
    private JLabel timeLabel;
    private JLabel guildInfoLabel;


    private final Mission mission;
    private final GuildMember guildMember;

    private ShapeIntersectCallback shapeIntersectCallback;

    private final Coords coords;
    private final Dimension dim;
    private MissionMarker[] hiddenMarkers;

    private static final int ROUNDING = 15;

    public DetailPanel(
            GuildMember member,
            Mission mission,
            Coords coords,
            Dimension dim,
            Dimension bounds,
            RunnableCallback missionSelectionCallback,
            ShapeIntersectCallback shapeIntersectCallback,
            Runnable unsetFlag
    ) {
        super(dim, ROUNDING, new Color(166,166,166, 196));
        this.setBorderColor(ColorUtils.CARBON);
        this.setBorderWidth(2);
        this.guildMember = member;
        this.mission = mission;
        this.shapeIntersectCallback = shapeIntersectCallback;
        this.coords = coords;
        this.dim = dim;
        this.thisPanel = this;

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        statusLabel = new JLabel(String.format("Mission status: %s", mission.getStatus().name().toLowerCase().replaceAll("_", " ")), SwingConstants.CENTER);
        Dimension labelSize = new Dimension(dim.width, ((int) (dim.height * 0.15)));
        statusLabel.setPreferredSize(labelSize);
        Color carbonColor = new Color(35, 35, 35);
        statusLabel.setForeground(carbonColor);
        statusLabel.setFont(FontUtils.getJomhuriaFont(20));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        this.add(statusLabel, gbc);
        gbc.gridy++;

        if (mission.getStatus() == MissionStatus.IN_PROGRESS) {
            this.timerThread = new Thread(this);
            this.timerThread.start();
            this.timeLabel = new JLabel();
            timeLabel.setPreferredSize(labelSize);
            timeLabel.setForeground(carbonColor);
            this.timeLabel.setFont(FontUtils.getJomhuriaFont(32));

            this.add(timeLabel, gbc);
            gbc.gridy++;
        }

        if (mission.getStatus() == MissionStatus.CREATED) {
            JLabel infoLabel = new JLabel("Required spells", SwingConstants.CENTER);
            infoLabel.setFont(FontUtils.getJomhuriaFont(20));
            infoLabel.setForeground(ColorUtils.CARBON);
            this.add(infoLabel, gbc);
            gbc.gridy++;
            mission.getRequiredSpellsSet().forEach(rs -> {
                JLabel requiredSpellLabel = new JLabel(String.format("%s lvl. %s", rs.getRequiredSpell().getName(), rs.getKnownLevel()), SwingConstants.CENTER);
                requiredSpellLabel.setFont(FontUtils.getJomhuriaFont(16));
                requiredSpellLabel.setForeground(ColorUtils.CARBON);
                this.add(requiredSpellLabel, gbc);
                gbc.gridy++;

            });
        }

        if (mission.getStatus() == MissionStatus.IN_PROGRESS || mission.getStatus() == MissionStatus.COMPLETED) {
            MissionAssignment[] missionAssignments = mission.getAssignments().toArray(new MissionAssignment[0]);

            guildInfoLabel = new JLabel(String.format("%s by: %s", mission.getStatus() == MissionStatus.IN_PROGRESS ? "undertaken" : "completed", mission.getAssignments().iterator().next().getGuildMember().getGuild().getGuildName()), SwingConstants.CENTER);
            guildInfoLabel.setForeground(ColorUtils.CARBON);
            guildInfoLabel.setFont(FontUtils.getJomhuriaFont(20));
            this.add(guildInfoLabel, gbc);
            gbc.gridy++;

            int singleExpeditionListPanelHeight = ((int) (dim.height * 0.15));

            for (int i = 0; i < missionAssignments.length; i++) {
                gbc.gridy += i;
                AssignedMemberPanel assignedMemberPanel = new AssignedMemberPanel(missionAssignments[i].getGuildMember(), dim, singleExpeditionListPanelHeight);
                this.add(assignedMemberPanel, gbc);
                if (missionAssignments[0].getMissionLeader() == missionAssignments[i].getGuildMember()){
                    assignedMemberPanel.makeLeader();
                }
            }

        }


        int startX = coords.x() - (dim.width / 2);
        int startY = coords.y() - (dim.height / 2);

        int endX = startX + dim.width;
        int endY = startY + dim.height;

        if (endX > bounds.width) {
            int overflow = endX - bounds.width;
            startX -= overflow;
        }

        if (endY > bounds.height) {
            int overflow = endY - bounds.height;
            startY -= overflow;
        }

        if (startX < 0) {
            startX = 0;
        }

        if (startY < 0) {
            startY = 0;
        }

        this.setBounds(startX, startY, dim.width, dim.height);

        if (mission.getStatus() == MissionStatus.CREATED) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    final boolean[] boolHolder = new boolean[1];
                    missionSelectionCallback.run(() -> {
                        boolHolder[0] = true;
                    });
                    if (!boolHolder[0]) {
                        unsetFlag.run();
                        thisPanel.setVisible(false);
                    }
                }
            });
        }
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                unsetFlag.run();
                thisPanel.setVisible(false);

                int accum = 0;
                for (MissionMarker marker : hiddenMarkers) {
                    accum++;
                    marker.setVisible(true);
                }
                System.out.println("made: " + accum + " markers visible");

//
//                if (mission.getStatus() == MissionStatus.IN_PROGRESS && timerThread != null) {
//                    timerThread.interrupt();
//                }
            }
        });
    }


    @Override
    public void addNotify() {
        super.addNotify();
        this.hiddenMarkers = shapeIntersectCallback.onShapeIntercept(new RoundRectangle2D.Float(getX(), getY(), getWidth(), getHeight(), ROUNDING, ROUNDING));
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            repaint();

            try {
                Thread.sleep(1000 / 12);
                long durationMillis = mission.getMissionCompletionTime() - (System.currentTimeMillis() - mission.getStartTimeMillis());
                Duration duration = Duration.ofMillis(durationMillis);
                if (durationMillis <= 0) {
                    mission.completeMission();
                    statusLabel.setText(String.format("Mission status: %s", mission.getStatus().name().toLowerCase().replaceAll("_", " ")));
                    guildInfoLabel.setText(String.format("completed by: %s", mission.getAssignments().iterator().next().getGuildMember().getGuild().getGuildName()));
                    timerThread.interrupt();
                } else {
                    long days = duration.toDays();
                    long hours = duration.toHours() % 24;
                    long minutes = duration.toMinutes() % 60;
                    long seconds = duration.getSeconds() % 60;
                    timeLabel.setText(String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds));
                    timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
                }
            } catch (InterruptedException ex) {
                timerThread.interrupt();
            }
        }
    }
}

