package Models.Panels;

import Models.Guild.GuildMember;
import Models.Mission.Mission;
import Models.Mission.MissionStatus;
import Models.Util.Coords;
import Models.Util.MissionSelectionCallback;
import Models.Util.ShapeIntersectCallback;
import Models.Util.SuperObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class GuildViewPanel extends JPanel {
    private GuildMember loggedInMember;

    private final int LABEL_HEIGHT = 40;
    private final int PANEL_HEIGHT = 520;

    private final int DESC_TILE_WIDTH = 150;
    private final int DESC_TILE_HEIGHT = 200;

    private final MissionSelectionCallback callback;

    private final List<MissionMarker> missionMarkers = new ArrayList<>();

    private final JLabel guildLabel;
    private final JLabel wizardLabel;

    private final JPanel thisPanel;

    public GuildViewPanel(MissionSelectionCallback missionSelectionCallback) {
        this.thisPanel = this;
        this.callback = missionSelectionCallback;
        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.setPreferredSize(new Dimension(getWidth(), LABEL_HEIGHT));
        labelPanel.setBackground(new Color(35,35,35));

        this.guildLabel = new JLabel();
        this.wizardLabel = new JLabel();
        labelPanel.add(guildLabel, BorderLayout.WEST);
        labelPanel.add(wizardLabel, BorderLayout.EAST);
        guildLabel.setForeground(Color.white);
        wizardLabel.setForeground(Color.white);
        Font font = new Font("Broadway", Font.BOLD, 20);
        guildLabel.setFont(font);
        wizardLabel.setFont(font);

        JLayeredPane backgroundImagePanel = new JLayeredPane();
        ImagePanel backgroundImage = new ImagePanel("resources/world-map.jpg");
        Dimension imageDim = new Dimension(backgroundImage.getOriginalImageWidth(), backgroundImage.getOriginalImageHeight());

        backgroundImagePanel.setPreferredSize(imageDim);

        backgroundImage.setBounds(0, 0, 720, PANEL_HEIGHT - LABEL_HEIGHT);
        backgroundImagePanel.add(backgroundImage, JLayeredPane.DEFAULT_LAYER);
        List<Mission> missions = SuperObject.getObjectsFromClass(Mission.class);
        final MissionMarker[] markers = new MissionMarker[missions.size()];
        for (int i = 0; i < missions.size(); i++) {
            Mission mission = missions.get(i);
            final MissionMarker[] markerHolder = new MissionMarker[1];

            Color color = new Color(168, 168, 168, 150);
            int finalI = i;
            MissionMarker missionMarker = new MissionMarker(
                    color,
                    mission.getTerritory().getCoords(),
                    mission,
                    missionSelectionCallback,
                    () -> {
                        backgroundImagePanel.add(new DetailPanel(
                                loggedInMember,
                                mission,
                                mission.getTerritory().getCoords(),
                                new Dimension(DESC_TILE_WIDTH, DESC_TILE_HEIGHT),
                                new Dimension(720, PANEL_HEIGHT - LABEL_HEIGHT), // TODO suboptimal, try to extract at runtime
                                color,
                                missionSelectionCallback,
                                (shape) -> {
                                    List<MissionMarker> markersDisappeared = new ArrayList<>();
                                    for (MissionMarker marker : markers) {
                                        if (shape.intersects(marker.getX1(), marker.getY1(), marker.getDiameterWithBorder(), marker.getDiameterWithBorder())){
                                            marker.setVisible(false);
                                            markersDisappeared.add(marker);
                                        }
                                    }
                                    return markersDisappeared.toArray(new MissionMarker[0]);
                                },
                                () -> {
                                    markers[finalI].setVisible(true);
                                }
                        ), JLayeredPane.PALETTE_LAYER);
                    }
            );
            markers[i] = missionMarker;

            missionMarkers.add(missionMarker);
            backgroundImagePanel.add(missionMarker, JLayeredPane.PALETTE_LAYER);
        }

        this.setLayout(new BorderLayout());
        this.add(labelPanel, BorderLayout.NORTH);
        this.add(backgroundImagePanel, BorderLayout.CENTER);
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag){
            onMount();
        }
    }

    private void onMount() {
        for (MissionMarker marker : missionMarkers) {
            marker.validateColor();
        }
    }

    public void setLoggedInMember(GuildMember member){
        this.loggedInMember = member;
        for (MissionMarker marker : missionMarkers) { // TODO suboptimal
            marker.setSelectedMember(member);
        }
        this.guildLabel.setText(String.format("Guild: %s", member.getGuild().getGuildName()));
        this.wizardLabel.setText(String.format("Wizard: %s", member.getName()));
    }
}

class DetailPanel extends JPanel implements Runnable{
    private final JPanel thisPanel;
    private final JLabel statusLabel;
    private Thread timerThread;
    private JLabel timeLabel;

    private final Mission mission;
    private final GuildMember guildMember;
    private RoundRectangle2D parentShape;

    private ShapeIntersectCallback shapeIntersectCallback;


    private final Coords coords;
    private final Dimension dim;
    private MissionMarker[] hiddenMarkers;
    public DetailPanel(
            GuildMember member,
            Mission mission,
            Coords coords,
            Dimension dim,
            Dimension bounds,
            Color color,
            MissionSelectionCallback missionSelectionCallback,
            ShapeIntersectCallback shapeIntersectCallback,
            Runnable unsetFlag
            ) {
        this.guildMember = member;
        this.mission = mission;
        this.shapeIntersectCallback = shapeIntersectCallback;
        this.coords = coords;
        this.dim = dim;
        this.thisPanel = this;

        Font infoFont = new Font("Britannic Bold", Font.PLAIN, 12);
        Font timerFont = new Font("Broadway", Font.BOLD, 20);

        this.setOpaque(false);
        this.setBackground(color);
        this.setPreferredSize(new Dimension(dim.width, dim.height));

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        statusLabel = new JLabel(String.format("Mission status: %s", mission.getStatus().name().toLowerCase().replaceAll("_", " ")), SwingConstants.CENTER);
        Dimension size = new Dimension(dim.width, ((int) (dim.height * 0.15)));
        statusLabel.setPreferredSize(size);
        statusLabel.setMinimumSize(size);
        statusLabel.setMaximumSize(size);
        statusLabel.setForeground(new Color(35,35,35));
        statusLabel.setFont(infoFont);

        this.add(statusLabel);

        if (mission.getStatus() == MissionStatus.IN_PROGRESS){
            this.timerThread = new Thread(this);
            this.timerThread.start();
            this.timeLabel = new JLabel();
            timeLabel.setPreferredSize(size);
            timeLabel.setMinimumSize(size);
            timeLabel.setMaximumSize(size);
            this.add(timeLabel);
            timeLabel.setForeground(new Color(35,35,35));
            this.timeLabel.setFont(timerFont);
        }

        int startX = coords.x() - (dim.width / 2);
        int startY = coords.y() - (dim.height / 2);

        int endX = startX + dim.width;
        int endY = startY + dim.height;
        if (endX > bounds.width){
            int overflow = endX - bounds.width;
            startX -= overflow;
        }

        if (endY > bounds.height){
            int overflow = endY - bounds.height;
            startY -= overflow;
        }

        if (startX < 0){
            startX = 0;
        }

        if (startY < 0){
            startY = 0;
        }

        this.setBounds(startX, startY, dim.width, dim.height);

        if (mission.getStatus() == MissionStatus.CREATED){
            this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    unsetFlag.run();
                    thisPanel.setVisible(false);
                    missionSelectionCallback.onMissionSelect(member, mission);
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

                if(mission.getStatus() == MissionStatus.IN_PROGRESS && timerThread != null){
                    timerThread.interrupt();
                }
            }
        });
    }


    @Override
    public void addNotify() {
        super.addNotify();

        this.parentShape = new RoundRectangle2D.Float(getX(), getY(), getWidth(), getHeight(), 15, 15);
        this.hiddenMarkers = shapeIntersectCallback.onShapeIntercept(parentShape);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        RoundRectangle2D mainPanel = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15);

        g2d.setColor(getBackground());
        g2d.fill(mainPanel);
        g2d.dispose();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()){
            repaint();

            try {
                Thread.sleep(1000/12);
                long durationMillis = mission.getMissionCompletionTimeMillis() - (System.currentTimeMillis() - mission.getStartTimeMillis());
                Duration duration = Duration.ofMillis(durationMillis);
                if (durationMillis <= 0){
                    mission.completeMission();
                    statusLabel.setText(String.format("Mission status: %s" ,mission.getStatus().name().toLowerCase().replaceAll("_", " ")));
                    timerThread.interrupt();
                }else {
                    long days = duration.toDays();
                    long hours = duration.toHours() % 24;
                    long minutes = duration.toMinutes() % 60;
                    long seconds = duration.getSeconds() % 60;
                    timeLabel.setText(String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds));
                    timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
                }
            }catch (InterruptedException ex){
                timerThread.interrupt();
            }
        }
    }
}


