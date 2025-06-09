package App.Panels.Panel;

import App.Models.Mission.MissionStatus;
import App.Util.MissionTimerService;
import App.Util.SuperObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class MainFrame extends JFrame {
    JPanel mainPanel;
    CardLayout cardLayout;

    public MainFrame() throws HeadlessException {
        this.mainPanel = new JPanel(new CardLayout());
        this.cardLayout = ((CardLayout) mainPanel.getLayout());


        MissionSelectionPanel missionSelectionPanel = new MissionSelectionPanel(
                ()-> cardLayout.previous(mainPanel),
                ()-> cardLayout.show(mainPanel, "STEP1")

        );

        GuildViewPanel guildViewPanel = new GuildViewPanel(
                (member, mission, runnable)->{
                    missionSelectionPanel.setMissionDispatcher(member);
                    missionSelectionPanel.setSelectedMission(mission);
                    missionSelectionPanel.populateMemberSelectionList();
                    missionSelectionPanel.setOnMissionCompletionCallback(runnable);
                    cardLayout.next(mainPanel);
                },
                ()-> cardLayout.show(mainPanel, "STEP1")
                );


        this.mainPanel.add(new LoginPanel((member)-> {
            guildViewPanel.setLoggedInMember(member);
            cardLayout.next(mainPanel);
        }), "STEP1");

        this.mainPanel.add(guildViewPanel, "STEP2");
        this.mainPanel.add(missionSelectionPanel, "STEP3");

        this.setContentPane(mainPanel);

        this.setVisible(true);

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    SuperObject.writeObjects();
                    MissionTimerService.shutdown();
                } catch (IOException ignored) { }
                System.exit(0);
            }
        });

        this.setResizable(false);
        this.setSize(new Dimension(720, 560));
    }


}
