package App.Callbacks;

import App.Models.Guild.GuildMember;
import App.Models.Mission.Mission;

@FunctionalInterface
public interface MissionSelectionCallback {
    void onMissionSelect(GuildMember member, Mission mission, Runnable callback);
}
