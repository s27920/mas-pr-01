package Models.Util;

import Models.Guild.GuildMember;

@FunctionalInterface
public interface MemberSelectionCallback {
    void onMemberSelect(GuildMember member);
}
