package Models.Mission;

import Models.Guild.GuildMember;
import Models.Util.SuperObject;

public class MissionAssignment extends SuperObject {
    GuildMember guildMember;
    Mission mission;

    public MissionAssignment(GuildMember guildMember, Mission mission) {
        setMission(mission);
        setGuildMember(guildMember);
        mission.addMissionAssignment(this);
        guildMember.addMissionAssignment(this);
    }

    public void setGuildMember(GuildMember guildMember) {
        this.guildMember = guildMember;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }
}
