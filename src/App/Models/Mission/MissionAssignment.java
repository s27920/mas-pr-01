package App.Models.Mission;

import App.Models.Guild.GuildMember;
import App.Util.SuperObject;

public class MissionAssignment extends SuperObject {
    private GuildMember guildMember;
    private Mission mission;
    private GuildMember missionLeader;

    public MissionAssignment(GuildMember guildMember, Mission mission) {
        setMission(mission);
        setGuildMember(guildMember);
        mission.addMissionAssignment(this);
        guildMember.addMissionAssignment(this);
    }

    public void setGuildMember(GuildMember guildMember) {
        this.guildMember = guildMember;
    }

    public void setMissionLeader(GuildMember member){
        if (!mission.getAssignments().stream().map(MissionAssignment::getGuildMember).toList().contains(member)){
            throw new IllegalArgumentException("Mission leader must be part of mission");
        }
        this.missionLeader = member;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public GuildMember getGuildMember() {
        return guildMember;
    }

    public GuildMember getMissionLeader() {
        return missionLeader;
    }

    public Mission getMission() {
        return mission;
    }
}
