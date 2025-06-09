package App.Models.Guild;

import App.Models.Magic.KnownSpell;
import App.Models.Magic.SpellTome;
import App.Models.Mission.Mission;
import App.Models.Mission.MissionAssignment;
import App.Models.Mission.MissionReward;
import App.Models.Mission.MissionStatus;
import App.Models.RealEstate.Ownership;
import App.Models.Wizard.Wizard;

import java.time.LocalDate;
import java.util.*;

public class GuildMember extends Wizard {
    private final LocalDate joinedDate;

    private Guild guild;
    private GuildRank rank;
    private MemberState memberState;

    private final Set<MissionReward> ownedRewards;
    private final Set<MissionAssignment> assignments;

    public GuildMember(String name, int chosenIcon, Guild guild) {
        super(name, chosenIcon);
        this.guild = guild;

        this.assignments = new HashSet<>();
        this.ownedRewards = new HashSet<>();

        this.rank = GuildRank.Apprentice;
        this.memberState = MemberState.ON_STANDBY;
        this.joinedDate = LocalDate.now();

        this.guild.addMember(this);

    }

    public MissionAssignment assignNewMission(Mission mission) {
        MissionAssignment assignment = new MissionAssignment(this, mission);
        this.assignments.add(assignment);
        this.memberState = MemberState.IN_MISSION;
        return assignment;
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

    public Set<MissionReward> getOwnedRewards() {
        return Collections.unmodifiableSet(ownedRewards);
    }

    public void addToOwnedRewards(MissionReward reward) {
        if (ownedRewards.add(reward)) {
            reward.setOwner(this);
        }
    }

    public void addMissionAssignment(MissionAssignment missionAssignment) {
        this.assignments.add(missionAssignment);
    }

    public int getTotalMissionsCompleted() {
        return this.assignments.stream().filter(a -> a.getMission().getStatus() == MissionStatus.COMPLETED).toList().size();
    }

    public MemberState getMemberState() {
        return memberState;
    }

    public void setMemberState(MemberState memberState) {
        this.memberState = memberState;
    }

    public void getPromoted() {
        this.rank = switch (this.rank) {
            case Apprentice -> GuildRank.Elder;
            case Master -> GuildRank.Master;
            case Elder -> (this.guild.getMembers().stream().noneMatch(m -> m.getRank() == GuildRank.Master)) ? GuildRank.Master : GuildRank.Elder;
        };
    }

}


