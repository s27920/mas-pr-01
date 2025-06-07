package App.Models.Guild;

import App.Models.Magic.KnownSpell;
import App.Models.Magic.SpellTome;
import App.Models.Mission.Mission;
import App.Models.Mission.MissionAssignment;
import App.Models.Mission.MissionStatus;
import App.Models.RealEstate.Ownership;
import App.Models.Wizard.Wizard;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

public class GuildMember extends Wizard {
    private final LocalDate joinedDate;

    private Guild guild;
    private GuildRank rank;
    private MemberState memberState;

    private final Set<MissionAssignment> assignments;

    public GuildMember(String name, int chosenIcon, Guild guild) {
        super(name, chosenIcon);
        this.guild = guild;

        this.assignments = new HashSet<>();

        this.rank = GuildRank.Apprentice;
        this.memberState = MemberState.ON_STANDBY;
        this.joinedDate = LocalDate.now();

        this.guild.addMember(this);

    }

    public GuildMember(String name, SortedSet<KnownSpell> knownSpells, Set<Ownership> ownedDomiciles, Set<SpellTome> ownedTomes, int chosenIcon, LocalDate joinedDate, Guild guild, GuildRank rank, MemberState memberState, Set<MissionAssignment> assignments) {
        super(name, knownSpells, ownedDomiciles, ownedTomes, chosenIcon);
        this.joinedDate = joinedDate;
        this.guild = guild;
        this.rank = rank;
        this.memberState = memberState;
        this.assignments = assignments;
    }

    public void assignNewMission(Mission mission){
        this.assignments.add(new MissionAssignment(this, mission));
        this.memberState = MemberState.IN_MISSION;
    }

    // TODO put this in guild
    public List<GuildMember> getValidGuildMembers(){
        return getGuild().getMembers().stream().filter(m->m.getMemberState() == MemberState.ON_STANDBY).toList();
    }

    public List<GuildMember> getInvalidGuildMembers(){
        return getGuild().getMembers().stream().filter(m->m.getMemberState() != MemberState.ON_STANDBY).toList();
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
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

    public int getTotalMissionsCompleted(){
        return this.assignments.stream().filter(a->a.getMission().getStatus() == MissionStatus.COMPLETED).toList().size();
    }

    public MemberState getMemberState() {
        return memberState;
    }

    public void setMemberState(MemberState memberState) {
        this.memberState = memberState;
    }

    public void getPromoted(){
        this.rank = switch (this.rank){
            case Apprentice -> GuildRank.Elder;
            case Master -> GuildRank.Master;
            case Elder -> (this.guild.getMembers().stream().noneMatch(m -> m.getRank() == GuildRank.Master)) ? GuildRank.Master : GuildRank.Elder;
        };
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


