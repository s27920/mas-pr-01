package Models.Guild;

import Models.Util.SuperObject;

import java.util.HashSet;
import java.util.Set;

public class Guild extends SuperObject {
    private Set<GuildMember> members;
    private String guildName;

    public Guild(Set<GuildMember> members, String guildName) {
        this.members = members;
        this.guildName = guildName;
    }

    public String getGuildName() {
        return guildName;
    }

    public Guild(String guildName) {
        this.members = new HashSet<>();
        this.guildName = guildName;
    }

    public Set<GuildMember> getMembers() {
        return members;
    }

    public void addMember(GuildMember guildMember){
        this.members.add(guildMember);
    }
}
