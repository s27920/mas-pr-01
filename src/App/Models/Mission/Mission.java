package App.Models.Mission;

import App.Models.Guild.GuildMember;
import App.Models.Guild.MemberState;
import App.Models.Guild.Territory;
import App.Models.Magic.KnownSpell;
import App.Models.Magic.RequiredSpell;
import App.Models.Magic.Spells.Spell;
import App.Util.MissionTimerService;
import App.Util.SuperObject;

import java.io.IOException;
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
    private String[] possibleScenarios;

    public static final long MISSION_COMPLETION_TIME_MILLIS_BASELINE = 15000;
    private long missionCompletionTime = -1;
    private long startTimeMillis;

    public static double missionTimeDevScalar = 1.0;


    public Mission(Territory territory, MissionDifficulty difficulty, String name, String description) {
        this.territory = territory;
        this.difficulty = difficulty;
        this.requiredSpellsSet = new HashSet<>();
        this.name = name;
        this.description = description;
        this.status = MissionStatus.CREATED;
        this.assignments = new HashSet<>();
    }

    public static void flipMissionTimeDevScalar() {
        if (isFlippedMissionTimeDevScalar()){
            Mission.missionTimeDevScalar = 1.0;
        }else {
            Mission.missionTimeDevScalar = 0.1;
        }
    }

    public static boolean isFlippedMissionTimeDevScalar(){
        if (missionTimeDevScalar > 0.95 && missionTimeDevScalar < 1.05) {
            return false;
        }
        return true;
    }

    public static void resetMissionScalar(){
        missionTimeDevScalar = 1.0;
    }

    public void startMission(){
        this.startTimeMillis = System.currentTimeMillis();
        this.status = MissionStatus.IN_PROGRESS;
        MissionTimerService.getInstance().registerMission(this);

        try {
            SuperObject.writeObjects();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean hasCompatibleSpell(KnownSpell knownSpell) {
        return requiredSpellsSet.stream().anyMatch(required -> required.getRequiredSpell().equals(knownSpell.getSpell()) && required.getKnownLevel() <= knownSpell.getMasteryLevel());
    }
    public void completeMission(){

        // TODO make injuries possible
        this.assignments.forEach(assignment -> assignment.getGuildMember().setMemberState(MemberState.ON_STANDBY));
        this.status = MissionStatus.COMPLETED;

        try {
            SuperObject.writeObjects();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean validateMissionCompleted(){
        return status == MissionStatus.IN_PROGRESS && getMissionRemainingTime() < 0;
    }

    public long getMissionRemainingTime(){
        long passedTime = System.currentTimeMillis() - startTimeMillis;
        return getMissionCompletionTime() - passedTime;
    }

    public long getMissionCompletionTime() {
        if (missionCompletionTime == -1){
            calculateMissionCompletionTime();
        }
        return missionCompletionTime;
    }
    public long getMissionCompletionTimeForced() {
        calculateMissionCompletionTime();
        return missionCompletionTime;
    }

    /**
     * calculates the time needed to complete a mission based on a number of factors such as:
     * <ul>
     *     <li>the composition of the expedition team</li>
     *     <li>whether the guild dispatching the expedition has control of the territory on which the mission takes place</li>
     *     <li>whether said territory has access restrictions</li>
     *     <li>mission difficulty level</li>
     *     <li>whether we are currently in dev mode (x10) mission completion speed</li>
     *   </ul>
     * is automatically called upon using <br/><br/>
     *   <b>getMissionCompletionTime() </b><br/><br/>
     * which checks whether the time has already been calculated <br/>
     * if yes it uses the ready-made number, otherwise calculating it on the fly <br/>
     * ideally should not be used as a standalone function <br/>
     * as it will result in unnecessary calculations
     */
    private void calculateMissionCompletionTime() {
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

        for (Map.Entry<Spell, Integer> requiredSpellMastery : knowsRequiredSpellsMaxLevel.entrySet()) {
            spellScalar *= 10.0 / requiredSpellMastery.getValue();
        }

        MissionAssignment missionAssignment = assignments.stream().findFirst().orElse(null);
        double territoryOwnerShipScalar;
        if (missionAssignment != null){
            territoryOwnerShipScalar = missionAssignment.getGuildMember().getGuild().getControlledTerritories().contains(territory) ? 1.0 : 1.5;
        } else {
            throw new IllegalArgumentException();
        }

        double territoryAccessibilityScalar = 1.0 + 0.1 * territory.getDangerLevel();

        this.missionCompletionTime = ((long) (Mission.MISSION_COMPLETION_TIME_MILLIS_BASELINE * difficultyScalar * spellScalar * territoryAccessibilityScalar * territoryOwnerShipScalar * missionTimeDevScalar));
    }

    @Override
    public void removeObj() {
        for (MissionAssignment missionAssignment : assignments) {
            missionAssignment.removeObj();
        }
        assignments.clear();
        super.removeObj();

    }

    public void addMissionAssignment(MissionAssignment missionAssignment) {
        if (this.assignments.size() < 4){
            this.assignments.add(missionAssignment);
        }else {
            throw new IllegalArgumentException("mission cannot have more than 4 assigned members");
        }
    }



    public void freeMembers(){
        assignments.forEach(ma-> ma.getGuildMember().setMemberState(MemberState.ON_STANDBY));
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

    public String[] getPossibleScenarios() {
        return possibleScenarios;
    }

    public void setPossibleScenarios(String[] possibleScenarios) {
        if (possibleScenarios != null && possibleScenarios.length > 0){
            this.possibleScenarios = possibleScenarios;
        }
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

    public MissionStatus getStatus() {
        return status;
    }

    public long getStartTimeMillis() {
        return startTimeMillis;
    }

    public void getAssignedToGuildMember(GuildMember guildMember, boolean isLeader){
        this.assignments.add(new MissionAssignment(guildMember, this));
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

