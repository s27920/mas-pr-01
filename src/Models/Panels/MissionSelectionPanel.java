package Models.Panels;

import Models.Guild.GuildMember;
import Models.Mission.Mission;

import javax.swing.*;

public class MissionSelectionPanel extends JPanel {
    GuildMember missionDispatcher;
    Mission selectedMission;

    public MissionSelectionPanel() {
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
