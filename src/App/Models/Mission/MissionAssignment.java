package App.Models.Mission;

import App.Models.Guild.GuildMember;
import App.Util.SuperObject;

public class MissionAssignment extends SuperObject {
    private GuildMember guildMember;
    private Mission mission;

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

    public GuildMember getGuildMember() {
        return guildMember;
    }

    public Mission getMission() {
        return mission;
    }


}
