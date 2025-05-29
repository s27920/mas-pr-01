package Models.Panels;

import Models.Guild.GuildMember;
import Models.Mission.Mission;
import Models.Util.MissionConfigurationCallback;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Set;

public class MissionSelectionPanel extends JPanel {
    GuildMember missionDispatcher;
    Mission selectedMission;
    JPanel memberSelectionPanel;
    JPanel missionDetailsPanel;
    
    private final int WIDTH = 720;
    private final int HEIGHT = 480;

    public MissionSelectionPanel() {
        this.setLayout(new BorderLayout());

        this.memberSelectionPanel = new JPanel();
        this.missionDetailsPanel = new JPanel();

        memberSelectionPanel.setPreferredSize(new Dimension(((int) (WIDTH * 0.5)), HEIGHT));
        missionDetailsPanel.setPreferredSize(new Dimension(((int) (WIDTH * 0.5)), HEIGHT));

        memberSelectionPanel.setBackground(Color.RED);
        missionDetailsPanel.setBackground(Color.GREEN);

        this.add(memberSelectionPanel, BorderLayout.WEST);
        this.add(missionDetailsPanel, BorderLayout.EAST);

        memberSelectionPanel.setLayout(new BoxLayout(this.memberSelectionPanel, BoxLayout.Y_AXIS));

        JPanel selectedPanel = new JPanel();
        JPanel listPanel = new JPanel();

        selectedPanel.setPreferredSize(new Dimension(((int) (WIDTH * 0.5)), ((int) (HEIGHT * 0.4))));
        listPanel.setPreferredSize(new Dimension(((int) (WIDTH * 0.5)), ((int) (HEIGHT * 0.6))));

        selectedPanel.setBackground(Color.BLUE);
        listPanel.setBackground(Color.MAGENTA);

        memberSelectionPanel.add(selectedPanel);
        memberSelectionPanel.add(listPanel);

        JScrollPane scrollPane = new JScrollPane();
        JPanel memberListPanel = new JPanel();

        scrollPane.setViewportView(memberListPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        memberListPanel.setLayout(new BoxLayout(memberListPanel, BoxLayout.Y_AXIS));
        memberSelectionPanel.add(scrollPane);

        Set<GuildMember> guildMemberList = missionDispatcher.getGuild().getMembers();

        for (GuildMember member : guildMemberList) {
            JPanel panel = new JPanel();
            panel.setPreferredSize(new Dimension(((int) (WIDTH * 0.5)), 100));
            panel.setBackground(new Color(((int) (Math.random() * 255)), ((int) (Math.random() * 255)), ((int) (Math.random() * 255))));
            memberListPanel.add(panel);
        }
    }

    public void setMissionDispatcher(GuildMember missionDispatcher) {
        this.missionDispatcher = missionDispatcher;
        System.out.println(missionDispatcher);
    }

    public void setSelectedMission(Mission selectedMission) {
        this.selectedMission = selectedMission;
        System.out.println(selectedMission);
    }
}
