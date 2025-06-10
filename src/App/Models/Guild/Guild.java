package App.Models.Guild;

import App.Models.Magic.MagicResource;
import App.Models.Mission.MissionReward;
import App.Models.Mission.MissionRewardType;
import App.Models.RealEstate.Castle;
import App.Util.SuperObject;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Guild extends SuperObject {
    private final Set<GuildMember> members;
    private final Set<Castle> ownedCastles;
    private final Set<Territory> controlledTerritories;
    private final Set<MagicResource> ownedMagicalResources;


    private final String guildName;
    private LocalDate creationDate;
    private int reputation;
    private String motto;
    private final int chosenIcon;


    public Guild(String guildName, String motto, int chosenIcon) {
        this.guildName = guildName;
        this.motto = motto;

        this.creationDate = LocalDate.now();
        this.reputation = 0;
        this.chosenIcon = chosenIcon;

        this.controlledTerritories = new HashSet<>();
        this.ownedCastles = new HashSet<>();
        this.members = new HashSet<>();
        ownedMagicalResources = new HashSet<>();
    }

    public Set<Territory> getControlledTerritories() {
        return Collections.unmodifiableSet(controlledTerritories);
    }

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

    public void addToOwnedMagicalResource(MagicResource magicResource){
        if (ownedMagicalResources.add(magicResource)) {
            magicResource.setOwner(this);
        }
    }

    public String getGuildName() {
        return guildName;
    }

    public String getMotto() {
        return motto;
    }

    public int getTotalMembers(){
        return members.size();
    }

    /**
     *
     * calculates the value of a guild's treasury using the value of its members owned treasure <br/>
     * (provided it is of type MissionRewardType.COIN) summed with the total value of the guild's owned Magical resources
     *
     * @return treasury value
     */
    public double getTreasuryValue(){
        double treasuryValue = 0.0;
        for (GuildMember m : members) {
            for (MissionReward mr : m.getOwnedRewards()) {
                if (mr.getRewardType() == MissionRewardType.COIN) {
                    treasuryValue += mr.getQuantity();
                }
            }
        }
        for (MagicResource ownedMagicalResource : ownedMagicalResources) {
            treasuryValue += ownedMagicalResource.getQuantity() * ownedMagicalResource.getUnitCost();
        }
        return treasuryValue;
    }
    public Set<GuildMember> getMembers() {
        return Collections.unmodifiableSet(members);
    }

    public List<GuildMember> getValidGuildMembers(){
        return getMembers().stream().filter(m->m.getMemberState() == MemberState.ON_STANDBY).toList();
    }

    public List<GuildMember> getInvalidGuildMembers(){
        return getMembers().stream().filter(m->m.getMemberState() != MemberState.ON_STANDBY).toList();
    }

    public int getChosenIcon() {
        return chosenIcon;
    }
}
