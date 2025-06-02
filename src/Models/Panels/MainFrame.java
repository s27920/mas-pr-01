package Models.Panels;

import Models.Mission.MissionStatus;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    JPanel mainPanel;
    CardLayout cardLayout;

    public MainFrame() throws HeadlessException {
        this.mainPanel = new JPanel(new CardLayout());
        this.cardLayout = ((CardLayout) mainPanel.getLayout());


        MissionSelectionPanel missionSelectionPanel = new MissionSelectionPanel(
                ()->{
                    cardLayout.previous(mainPanel);
                },
                () -> {
//                    cardLayout.previous(mainPanel);
                }
        );

        GuildViewPanel guildViewPanel = new GuildViewPanel((member, mission)->{
            missionSelectionPanel.setMissionDispatcher(member);
            missionSelectionPanel.setSelectedMission(mission);

            missionSelectionPanel.populateMemberSelectionList();
            if (mission.getStatus() == MissionStatus.CREATED){
                if (member.getValidGuildMembers().size() >= 2){
                    cardLayout.next(mainPanel);
                }else {
                    System.out.println("not enough members");
                }
            }
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
        this.setSize(new Dimension(720, 560));
    }
}
