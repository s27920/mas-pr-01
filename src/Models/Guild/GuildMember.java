package Models.Guild;

import Models.Magic.KnownSpell;
import Models.Magic.SpellTome;
import Models.Mission.Mission;
import Models.Mission.MissionAssignment;
import Models.RealEstate.Ownership;
import Models.Util.SuperObject;
import Models.Wizard;

import java.util.Set;

public class GuildMember extends Wizard {
    private GuildRank rank;
    private Guild guild;
    private Set<MissionAssignment> assignments;

    public GuildMember(String name, GuildRank rank, Guild guild) {
        super(name);
        this.rank = rank;
        this.guild = guild;
    }

    public GuildMember(String name, int chosenIcon, GuildRank rank, Guild guild) {
        super(name, chosenIcon);
        this.rank = rank;
        this.guild = guild;
        guild.addMember(this);
    }

    public GuildMember(String name, Set<KnownSpell> knownSpells, Set<Ownership> ownedDomiciles, Set<SpellTome> ownedTomes, GuildRank rank, Guild guild) {
        super(name, knownSpells, ownedDomiciles, ownedTomes);
        this.rank = rank;
        this.guild = guild;
    }

    public void assignNewMission(Mission mission){
        assignments.add(new MissionAssignment(this, mission));
    }

    public Guild getGuild() {
        return guild;
    }

    public GuildRank getRank() {
        return rank;
    }

    public void addMissionAssignment(MissionAssignment missionAssignment) {

        this.assignments.add(missionAssignment);
    }

    @Override
    public String toString() {
        return "GuildMember{" + super.toString() +
                "rank=" + rank +
                ", guild=" + guild +
                ", assignments=" + assignments +
                '}';
    }
}
