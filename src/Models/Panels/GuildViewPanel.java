package Models.Panels;

import Models.Guild.GuildMember;
import Models.Guild.Territory;
import Models.Mission.Mission;
import Models.Mission.MissionDifficulty;
import Models.Util.ColorUtils;
import Models.Util.Coords;
import Models.Util.MissionSelectionCallback;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class GuildViewPanel extends JPanel {
    private GuildMember loggedInMember;

    private final int LABEL_HEIGHT = 50;
    private final int PANEL_HEIGHT = 480;

    MissionSelectionCallback callback;

    List<MissionMarker> missionMarkers = new ArrayList<>();


    public GuildViewPanel(MissionSelectionCallback missionSelectionCallback) {
        this.callback = missionSelectionCallback;
        this.setSize(new Dimension(getWidth(), PANEL_HEIGHT));
        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.setPreferredSize(new Dimension(getWidth(), LABEL_HEIGHT));
        labelPanel.setBackground(new Color(65, 0, 0));

        JLayeredPane backgroundImagePanel = new JLayeredPane();
        backgroundImagePanel.setPreferredSize(new Dimension(getWidth(), PANEL_HEIGHT - LABEL_HEIGHT)); // TODO make these calculated dynamically maybe

        ImagePanel backgroundImage = new ImagePanel("resources/world-map.jpg");
        backgroundImage.setBounds(0, 0, 720, PANEL_HEIGHT - LABEL_HEIGHT);
        backgroundImagePanel.add(backgroundImage, JLayeredPane.DEFAULT_LAYER);


        for (Mission mission : getMockData()) {
            MissionMarker missionMarker = new MissionMarker(
                    new Color(168, 168, 168, 150),
                    new Coords(mission.getTerritory().getCoords().x(), mission.getTerritory().getCoords().y()),
                    new PointSize(20, 20),
                    missionSelectionCallback,
                    mission
            );
            missionMarkers.add(missionMarker);
            backgroundImagePanel.add(missionMarker, JLayeredPane.PALETTE_LAYER);
        }

        this.setLayout(new BorderLayout());
        this.add(labelPanel, BorderLayout.NORTH);
        this.add(backgroundImagePanel, BorderLayout.CENTER);
    }

    public void setLoggedInMember(GuildMember member){
        this.loggedInMember = member;
        for (MissionMarker marker :
                missionMarkers) {
            marker.setSelectedMember(member);
        }
    }

    public List<Mission> getMockData(){

        List<Mission> missions1 = new ArrayList<>();
        missions1.add(new Mission(new Territory("territory 1", new Coords(180, 120)), MissionDifficulty.Easy));
        missions1.add(new Mission(new Territory("territory 2", new Coords(360, 120)), MissionDifficulty.Medium));
        missions1.add(new Mission(new Territory("territory 3", new Coords(180, 240)), MissionDifficulty.Medium));
        missions1.add(new Mission(new Territory("territory 4", new Coords(360, 240)), MissionDifficulty.Hard));
        return missions1;
    }
}

class MissionMarker extends JPanel{

    private Color pointColor;
    private Color borderColor;
    private final JPanel thisPoint;
    private GuildMember selectedMember;

    public void setSelectedMember(GuildMember selectedMember) {
        this.selectedMember = selectedMember;
    }

    public MissionMarker(
            Color color,
            Coords coords,
            PointSize size,
            MissionSelectionCallback callback,
            Mission mission
    ) {
        this.pointColor = color;
        this.thisPoint = this;

        this.borderColor = switch (mission.getDifficulty()) {
            case Easy -> Color.GREEN;
            case Medium -> Color.YELLOW;
            case Hard -> Color.RED;
        };

        this.setBounds(coords.x(), coords.y(), size.width() + 2, size.height() + 2);
        this.setOpaque(false);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                callback.onMissionSelect(selectedMember, mission);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                pointColor = ColorUtils.darkenColor(pointColor, 25);
                borderColor = ColorUtils.darkenColor(borderColor, 25);
                thisPoint.revalidate();
                thisPoint.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                pointColor = ColorUtils.lightenColor(pointColor, 25);
                borderColor = ColorUtils.lightenColor(borderColor, 25);
                thisPoint.revalidate();
                thisPoint.repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = ((Graphics2D) g.create());

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(pointColor);
        g2d.fillOval(0, 0, 20, 20);

        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.drawOval(0, 0, 20, 20);

    }

}


record PointSize(int width, int height){ }