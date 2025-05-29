package Models.Util;

import Models.Guild.GuildMember;
import Models.Mission.Mission;

public interface MissionSelectionCallback {
    void onMissionSelect(GuildMember member, Mission mission);
}
