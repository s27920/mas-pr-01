package App.Models.Mission;

import App.Models.Guild.GuildMember;
import App.Models.Guild.MemberState;
import App.Models.Guild.Territory;
import App.Models.Magic.RequiredSpell;
import App.Models.Magic.Spell;
import App.Util.MissionTimerService;
import App.Util.SuperObject;

import java.util.*;

public class Mission extends SuperObject {
    private Territory territory;
    private MissionDifficulty difficulty;
    private MissionStatus status;

    private Set<MissionReward> rewards;
    private Set<MissionAssignment> assignments;
    private final Set<RequiredSpell> requiredSpellsSet;

    private String name;
    private String description;

    private long startTimeMillis;
    private long missionCompletionTimeMillis;

    private final Set<Spell> requiredSpells;

    public Mission(Territory territory, MissionDifficulty difficulty, String name, String description, Set<Spell> requiredSpells, Set<MissionReward> rewards) {
        this.rewards = rewards;
        this.territory = territory;
        this.difficulty = difficulty;
        this.requiredSpellsSet = new HashSet<>();
        this.name = name;
        this.description = description;
        this.requiredSpells = new HashSet<>();
        this.status = MissionStatus.CREATED;
        this.assignments = new HashSet<>();

        addToRequiredSpells(requiredSpells.toArray(new Spell[0]));
    }

    public void startMission(Runnable callback){
        MissionTimerService.getInstance().registerMission(this, callback);
        this.status = MissionStatus.IN_PROGRESS;
        this.startTimeMillis = System.currentTimeMillis();
    }
    public void completeMission(){

        // TODO make injuries possible
        this.assignments.forEach(assignment -> assignment.getGuildMember().setMemberState(MemberState.ON_STANDBY));
        this.status = MissionStatus.COMPLETED;
    }

    public void setMissionCompletionTimeMillis(long millis){
        if (millis < 0){
            throw new IllegalArgumentException(">:( no negative milliseconds");
        }
        this.missionCompletionTimeMillis = millis;
    }

    public MissionStatus getStatus() {
        return status;
    }

    public long getStartTimeMillis() {
        return startTimeMillis;
    }

    public long getMissionCompletionTimeMillis() {
        return missionCompletionTimeMillis;
    }


    public Set<MissionAssignment> getAssignments() {
        return assignments;
    }

    public Territory getTerritory() {
        return territory;
    }

    public MissionDifficulty getDifficulty() {
        return difficulty;
    }

    public void setTerritory(Territory territory) {
        this.territory = territory;
    }

    public void setRewards(Set<MissionReward> rewards) {
        this.rewards = rewards;
    }

    public String getName() {
        return name;
    }

    public Set<Spell> getRequiredSpells() {
        return requiredSpells;
    }

    public String getDescription() {
        return description;
    }

    public void setAssignments(Set<MissionAssignment> assignments) {
        this.assignments = assignments;
    }

    public void getAssignedToGuildMember(GuildMember guildMember){
        this.assignments.add(new MissionAssignment(guildMember, this));
    }

    public void addMissionAssignment(MissionAssignment missionAssignment) {
        this.assignments.add(missionAssignment);
    }

    public void addMissionRequirement(RequiredSpell requiredSpell) {
        this.requiredSpellsSet.add(requiredSpell);
    }

    public void addToRequiredSpells(Spell... spells){
        for (Spell spell : spells) {
            System.out.println(spell.getName());
            this.requiredSpellsSet.add(new RequiredSpell(spell, this));
        }
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
    }

    public Set<RequiredSpell> getRequiredSpellsSet() {
        return Collections.unmodifiableSet(this.requiredSpellsSet);
    }
}