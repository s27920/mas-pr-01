package Models.Panels;

import Models.Guild.Guild;
import Models.Guild.GuildMember;
import Models.Guild.Territory;
import Models.Mission.Mission;
import Models.Mission.MissionDifficulty;
import Models.Util.Coords;
import Models.Util.MissionSelectionCallback;
import Models.Util.PointSize;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GuildViewPanel extends JPanel {
    private GuildMember loggedInMember;

    private final int LABEL_HEIGHT = 50;
    private final int PANEL_HEIGHT = 480;

    MissionSelectionCallback callback;

    List<MissionMarker> missionMarkers = new ArrayList<>();

    JLabel guildLabel;
    JLabel wizardLabel;

    public GuildViewPanel(MissionSelectionCallback missionSelectionCallback) {
        this.callback = missionSelectionCallback;
        this.setSize(new Dimension(getWidth(), PANEL_HEIGHT));
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
        for (MissionMarker marker : missionMarkers) { // TODO suboptimal
            marker.setSelectedMember(member);
        }
        this.guildLabel.setText(String.format("Guild: %s", member.getGuild().getGuildName()));
        this.wizardLabel.setText(String.format("Wizard: %s", member.getName()));

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


