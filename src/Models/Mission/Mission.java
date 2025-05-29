package Models.Mission;

import Models.Guild.GuildMember;
import Models.Guild.Territory;
import Models.Util.SuperObject;

import java.util.Set;

public class Mission extends SuperObject {
    private Territory territory;
    private MissionDifficulty difficulty;

    private Set<MissionReward> rewards;
    private Set<MissionAssignment> assignments;


    public Mission(Territory territory, Set<MissionReward> rewards) {
        setTerritory(territory);
        setRewards(rewards);
        setAssignments(null);
    }

    public Mission(Territory territory, MissionDifficulty difficulty) {
        this.territory = territory;
        this.difficulty = difficulty;
    }

    public Territory getTerritory() {
        return territory;
    }

    public MissionDifficulty getDifficulty() {
        return difficulty;
    }

    public void setTerritory(Territory territory) {
        this.territory = territory;
    }

    public void setRewards(Set<MissionReward> rewards) {
        this.rewards = rewards;
    }

    public void setAssignments(Set<MissionAssignment> assignments) {
        this.assignments = assignments;
    }

    public void getAssignedToGuildMember(GuildMember guildMember){
        this.assignments.add(new MissionAssignment(guildMember, this));
    }

    public void addMissionAssignment(MissionAssignment missionAssignment) {
        this.assignments.add(missionAssignment);
    }
}