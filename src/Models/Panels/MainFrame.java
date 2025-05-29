package Models.Panels;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    JPanel mainPanel;
    CardLayout cardLayout;

    public MainFrame() throws HeadlessException {
        this.mainPanel = new JPanel(new CardLayout());
        this.cardLayout = ((CardLayout) mainPanel.getLayout());


        MissionSelectionPanel missionSelectionPanel = new MissionSelectionPanel();

        GuildViewPanel guildViewPanel = new GuildViewPanel((member, territory)->{
            missionSelectionPanel.setMissionDispatcher(member);
            missionSelectionPanel.setSelectedMission(territory);
            cardLayout.next(mainPanel);
        });

        this.mainPanel.add(new LoginPanel((member)-> {
            guildViewPanel.setLoggedInMember(member);
            cardLayout.next(mainPanel);
        }), "STEP1");

        this.mainPanel.add(guildViewPanel, "STEP2");
        this.mainPanel.add(missionSelectionPanel, "STEP3");

        this.setContentPane(mainPanel);

        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(new Dimension(720, 480));
    }
}
