package App.Models.Mission;

import App.Models.Guild.GuildMember;
import App.Models.Guild.MemberState;
import App.Models.Guild.Territory;
import App.Models.Magic.KnownSpell;
import App.Models.Magic.RequiredSpell;
import App.Models.Magic.Spell;
import App.Util.MissionTimerService;
import App.Util.SuperObject;

import java.util.*;

public class Mission extends SuperObject {
    private Territory territory;
    private final MissionDifficulty difficulty;
    private MissionStatus status;

    private Set<MissionReward> rewards;
    private Set<MissionAssignment> assignments;
    private final Set<RequiredSpell> requiredSpellsSet;

    private String name;
    private String description;

    public static final long MISSION_COMPLETION_TIME_MILLIS_BASELINE = 15000;
    private long missionCompletionTime;
    private long startTimeMillis;

    public Mission(Territory territory, MissionDifficulty difficulty, String name, String description, Set<Spell> requiredSpells, Set<MissionReward> rewards) {
        this.rewards = rewards;
        this.territory = territory;
        this.difficulty = difficulty;
        this.requiredSpellsSet = new HashSet<>();
        this.name = name;
        this.description = description;
        this.status = MissionStatus.CREATED;
        this.assignments = new HashSet<>();

        addToRequiredSpells(requiredSpells.toArray(new Spell[0]));
    }

    public void startMission(long missionCompletionTimeMillis, Runnable callback){
        MissionTimerService.getInstance().registerMission(missionCompletionTimeMillis, callback);
        this.status = MissionStatus.IN_PROGRESS;
        this.startTimeMillis = System.currentTimeMillis();
    }
    public void completeMission(){

        // TODO make injuries possible
        this.assignments.forEach(assignment -> assignment.getGuildMember().setMemberState(MemberState.ON_STANDBY));
        this.status = MissionStatus.COMPLETED;
    }

    public MissionStatus getStatus() {
        return status;
    }

    public long getStartTimeMillis() {
        return startTimeMillis;
    }

    public void calculateMissionCompletionTime() {
        double difficultyScalar = 1;
        difficultyScalar += getDifficulty().ordinal() / 4.0;

        Set<Spell> tmpCopySet = new HashSet<>();
        getRequiredSpellsSet().forEach(ks -> tmpCopySet.add(ks.getRequiredSpell()));

        final HashMap<Spell, Integer> knowsRequiredSpellsMaxLevel = new HashMap<>();

        double spellScalar = 1.0;

        for (MissionAssignment ma : assignments) {
            for (KnownSpell ks : ma.getGuildMember().getKnownSpells()) {
                if (tmpCopySet.contains(ks.getSpell())) {
                    if (knowsRequiredSpellsMaxLevel.containsKey(ks.getSpell())) {
                        // treat as bonus spell
                        spellScalar *= 1 - 0.005 * ks.getMasteryLevel();
                    }
                    knowsRequiredSpellsMaxLevel.put(ks.getSpell(), ks.getMasteryLevel());
                } else {
                    // treat as bonus spell
                    spellScalar *= 1 - 0.005 * ks.getMasteryLevel();
                }
            }
        }

        // required Spells
        for (Map.Entry<Spell, Integer> requiredSpellMastery : knowsRequiredSpellsMaxLevel.entrySet()) {
            spellScalar *= 10.0 / requiredSpellMastery.getValue();
        }

        this.missionCompletionTime = ((long) (Mission.MISSION_COMPLETION_TIME_MILLIS_BASELINE * difficultyScalar * spellScalar));
    }

    public long getMissionCompletionTime() {
        return missionCompletionTime;
    }

    public Set<MissionAssignment> getAssignments() {
        return Collections.unmodifiableSet(assignments);
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
            this.requiredSpellsSet.add(new RequiredSpell(spell, this));
        }
    }

    public Set<RequiredSpell> getRequiredSpellsSet() {
        return Collections.unmodifiableSet(this.requiredSpellsSet);
    }
}