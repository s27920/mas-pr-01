package App.Panels.Components;

import App.Callbacks.MissionSelectionCallback;
import App.Callbacks.RunnableCallback;
import App.Callbacks.ShapeIntersectCallback;
import App.Models.Guild.GuildMember;
import App.Models.Mission.Mission;
import App.Models.Mission.MissionAssignment;
import App.Models.Mission.MissionStatus;
import App.Panels.GuiUtil.ImagePanel;
import App.Panels.GuiUtil.RoundedPanel;
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
            Color color,
            RunnableCallback missionSelectionCallback,
            ShapeIntersectCallback shapeIntersectCallback,
            Runnable unsetFlag
    ) {
        super(dim, ROUNDING, color);
        this.guildMember = member;
        this.mission = mission;
        this.shapeIntersectCallback = shapeIntersectCallback;
        this.coords = coords;
        this.dim = dim;
        this.thisPanel = this;

        Font infoFont = new Font("Britannic Bold", Font.PLAIN, 12);
        Font timerFont = new Font("Broadway", Font.BOLD, 20);

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        statusLabel = new JLabel(String.format("Mission status: %s", mission.getStatus().name().toLowerCase().replaceAll("_", " ")), SwingConstants.CENTER);
        Dimension labelSize = new Dimension(dim.width, ((int) (dim.height * 0.15)));
        statusLabel.setPreferredSize(labelSize);
        Color carbonColor = new Color(35, 35, 35);
        statusLabel.setForeground(carbonColor);
        statusLabel.setFont(infoFont);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        this.add(statusLabel, gbc);

        int currentRow = 1;

        if (mission.getStatus() == MissionStatus.IN_PROGRESS) {
            this.timerThread = new Thread(this);
            this.timerThread.start();
            this.timeLabel = new JLabel();
            timeLabel.setPreferredSize(labelSize);
            timeLabel.setForeground(carbonColor);
            this.timeLabel.setFont(timerFont);

            gbc.gridy = currentRow;
            this.add(timeLabel, gbc);
            currentRow++;
        }

        if (mission.getStatus() == MissionStatus.IN_PROGRESS || mission.getStatus() == MissionStatus.COMPLETED) {
            Color transparentColor = new Color(0, 0, 0, 0);
            MissionAssignment[] missionAssignments = mission.getAssignments().toArray(new MissionAssignment[0]);

            int singleExpeditionListPanelHeight = ((int) (dim.height * 0.15));

            for (int i = 0; i < missionAssignments.length; i++) {
                MissionAssignment assignment = missionAssignments[i];
                GuildMember assignedGuildMember = assignment.getGuildMember();

                JPanel assignedMemberPanel = new JPanel(new BorderLayout());
                assignedMemberPanel.setPreferredSize(new Dimension(dim.width, singleExpeditionListPanelHeight));
                assignedMemberPanel.setBackground(transparentColor);

                JPanel assignedMemberDetailPanel = new JPanel(new BorderLayout());
                int assignedMemberDetailPanelWidth = (int) (dim.width * 0.9);
                int paddingWidth = (dim.width - assignedMemberDetailPanelWidth) / 2;

                assignedMemberDetailPanel.setPreferredSize(new Dimension(assignedMemberDetailPanelWidth, singleExpeditionListPanelHeight));
                assignedMemberDetailPanel.setBackground(transparentColor);

                ImagePanel iconPanel = ImagePanel.getGuildMemberIcon(assignedGuildMember.getChosenIcon());
                JLabel memberName = new JLabel(assignedGuildMember.getName(), SwingConstants.CENTER);

                iconPanel.setPreferredSize(new Dimension(singleExpeditionListPanelHeight, singleExpeditionListPanelHeight)); // intentionally duplicated as icons are NxN
                iconPanel.setBackground(transparentColor);
                memberName.setPreferredSize(new Dimension(assignedMemberDetailPanelWidth - singleExpeditionListPanelHeight, singleExpeditionListPanelHeight));
                memberName.setFont(infoFont);
                memberName.setForeground(carbonColor);

                assignedMemberDetailPanel.add(iconPanel, BorderLayout.WEST);
                assignedMemberDetailPanel.add(memberName, BorderLayout.EAST);


                JPanel fillerPanelLeft = new JPanel();
                JPanel fillerPanelRight = new JPanel();

                fillerPanelLeft.setPreferredSize(new Dimension(paddingWidth, singleExpeditionListPanelHeight));
                fillerPanelLeft.setBackground(transparentColor);

                fillerPanelRight.setPreferredSize(new Dimension(paddingWidth, singleExpeditionListPanelHeight));
                fillerPanelRight.setBackground(transparentColor);

                assignedMemberPanel.add(fillerPanelLeft, BorderLayout.WEST);
                assignedMemberPanel.add(fillerPanelRight, BorderLayout.EAST);
                assignedMemberPanel.add(assignedMemberDetailPanel, BorderLayout.CENTER);

                gbc.gridy = currentRow + i;
                this.add(assignedMemberPanel, gbc);
            }

            currentRow += missionAssignments.length;
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
                    missionSelectionCallback.run(()-> {
                        boolHolder[0] = true;
                    });
                    System.out.println(boolHolder[0]);
                    if (!boolHolder[0]){
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
