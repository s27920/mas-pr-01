package App.Models.Mission;

import App.Models.Guild.GuildMember;
import App.Models.Magic.KnownSpell;
import App.Models.Magic.Spell;
import App.Types.SpellLevelTuple;
import App.Util.SuperObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
