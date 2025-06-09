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
        super(dim, ROUNDING, new Color(166,166,166, 128));
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
            mission.getRequiredSpellsSet().forEach(rs -> {
                RoundedPanel requireSpellPanel = new RoundedPanel(5);
            });
        }

        if (mission.getStatus() == MissionStatus.IN_PROGRESS || mission.getStatus() == MissionStatus.COMPLETED) {
            MissionAssignment[] missionAssignments = mission.getAssignments().toArray(new MissionAssignment[0]);

            int singleExpeditionListPanelHeight = ((int) (dim.height * 0.15));

            gbc.gridy++;
            JLabel missionLeaderLabel = new JLabel("mission leader", SwingConstants.CENTER);
            missionLeaderLabel.setForeground(new Color(0x6B6B6B));
            missionLeaderLabel.setFont(FontUtils.getJomhuriaFont(16));
            this.add(missionLeaderLabel, gbc);
            gbc.gridy++;
            this.add(new AssignedMemberPanel(missionAssignments[0].getGuildMember(), dim, singleExpeditionListPanelHeight), gbc);

            gbc.gridy++;
            JLabel missionMemberLabel = new JLabel(String.format("mission member%s", missionAssignments.length == 2 ? "" : "s"), SwingConstants.CENTER);
            missionMemberLabel.setFont(FontUtils.getJomhuriaFont(16));
            missionMemberLabel.setForeground(Color.WHITE);
            this.add(missionMemberLabel, gbc);

            gbc.gridy++;
            for (int i = 1; i < missionAssignments.length; i++) {
                gbc.gridy += i;
                this.add(new AssignedMemberPanel(missionAssignments[i].getGuildMember(), dim, singleExpeditionListPanelHeight), gbc);
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

                for (MissionMarker marker : hiddenMarkers) {
                    marker.setVisible(true);
                }

                if (mission.getStatus() == MissionStatus.IN_PROGRESS && timerThread != null) {
                    timerThread.interrupt();
                }
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

class AssignedMemberPanel extends JPanel {
    private final GuildMember guildMember;
    private final Dimension panelDimension;
    private final int panelHeight;

    public AssignedMemberPanel(GuildMember guildMember, Dimension panelDimension, int panelHeight) {
        this.guildMember = guildMember;
        this.panelDimension = panelDimension;
        this.panelHeight = panelHeight;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(panelDimension.width, panelHeight));
        setBackground(ColorUtils.TRANSPARENT);

        JPanel detailPanel = createDetailPanel();

        JPanel[] fillerPanels = createFillerPanels();

        add(fillerPanels[0], BorderLayout.WEST);
        add(fillerPanels[1], BorderLayout.EAST);
        add(detailPanel, BorderLayout.CENTER);
    }

    private JPanel createDetailPanel() {
        JPanel detailPanel = new JPanel(new BorderLayout());
        int detailPanelWidth = (int) (panelDimension.width * 0.9);

        detailPanel.setPreferredSize(new Dimension(detailPanelWidth, panelHeight));
        detailPanel.setBackground(ColorUtils.TRANSPARENT);

        ImagePanel iconPanel = ImagePanel.getGuildMemberIcon(guildMember.getChosenIcon());
        iconPanel.setPreferredSize(new Dimension(panelHeight, panelHeight));
        iconPanel.setBackground(ColorUtils.TRANSPARENT);

        JLabel memberName = new JLabel(guildMember.getName(), SwingConstants.LEFT);
        memberName.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        memberName.setPreferredSize(new Dimension(detailPanelWidth - panelHeight, panelHeight));
        memberName.setFont(FontUtils.getJomhuriaFont(20));
        memberName.setForeground(ColorUtils.DARK_PURPLE);

        detailPanel.add(iconPanel, BorderLayout.WEST);
        detailPanel.add(memberName, BorderLayout.EAST);

        return detailPanel;
    }

    private JPanel[] createFillerPanels() {
        int detailPanelWidth = (int) (panelDimension.width * 0.9);
        int paddingWidth = (panelDimension.width - detailPanelWidth) / 2;

        JPanel fillerLeft = new JPanel();
        JPanel fillerRight = new JPanel();

        fillerLeft.setPreferredSize(new Dimension(paddingWidth, panelHeight));
        fillerLeft.setBackground(ColorUtils.TRANSPARENT);

        fillerRight.setPreferredSize(new Dimension(paddingWidth, panelHeight));
        fillerRight.setBackground(ColorUtils.TRANSPARENT);

        return new JPanel[]{fillerLeft, fillerRight};
    }

    public GuildMember getGuildMember() {
        return guildMember;
    }
}
