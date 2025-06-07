package App.Models.Guild;

import App.Models.RealEstate.Castle;
import App.Util.SuperObject;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Guild extends SuperObject {
    private final Set<GuildMember> members;
    private final Set<Castle> ownedCastles;
    private final Set<Territory> controlledTerritories;


    private final String guildName;
    private LocalDate creationDate;
    private int reputation;
    private String motto;


    // new Guild creation
    public Guild(String guildName, String motto) {
        this.guildName = guildName;
        this.motto = motto;

        this.creationDate = LocalDate.now();
        this.reputation = 0;

        this.controlledTerritories = new HashSet<>();
        this.ownedCastles = new HashSet<>();
        this.members = new HashSet<>();
    }

    public Guild(Set<GuildMember> members, String guildName) {
        this.members = members;
        this.guildName = guildName;

        this.controlledTerritories = new HashSet<>();
        this.ownedCastles = new HashSet<>();
    }

    // create 2-way links
    public void addToOwnedTerritories(Territory territory){
        if (this.controlledTerritories.add(territory)){
            territory.setGuild(this);
        }
    }
    public void purchaseCastle(Castle castle){
        if (this.ownedCastles.add(castle)){
            castle.setOwnsCastle(this);
        }
    }
    public void addMember(GuildMember guildMember){
        if (this.members.add(guildMember)){
            guildMember.setGuild(this);
        }
    }

    public String getGuildName() {
        return guildName;
    }

    public int getTotalMembers(){
        return members.size();
    }

    public int getTreasuryValue(){
        return -1; // TODO implement
    }
    public Set<GuildMember> getMembers() {
        return members;
    }
}
