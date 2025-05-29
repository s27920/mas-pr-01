package Models.Util;

import Models.Guild.GuildMember;
import Models.Mission.Mission;

public interface MissionConfigurationCallback {
    void onMissionSelection(GuildMember dispatcher, Mission mission);
}
