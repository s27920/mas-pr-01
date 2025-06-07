package App.Callbacks;

import App.Models.Guild.GuildMember;

@FunctionalInterface
public interface MemberSelectionCallback {
    void onMemberSelect(GuildMember member);
}
