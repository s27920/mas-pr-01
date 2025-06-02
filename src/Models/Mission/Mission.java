package Models.Mission;

import Models.Guild.GuildMember;
import Models.Guild.MemberState;
import Models.Guild.Territory;
import Models.Magic.Spell;
import Models.Util.MissionTimerService;
import Models.Util.SuperObject;

import java.util.HashSet;
import java.util.Set;

public class Mission extends SuperObject {
    private Territory territory;
    private MissionDifficulty difficulty;
    private MissionStatus status;

    private Set<MissionReward> rewards;
    private Set<MissionAssignment> assignments;

    private String name;
    private String description;

    private long startTimeMillis;
    private long missionCompletionTimeMillis;

    private final Set<Spell> requiredSpells;

    public Mission(Territory territory, MissionDifficulty difficulty, String name, String description, Set<Spell> requiredSpells, Set<MissionReward> rewards) {
        this.rewards = rewards;
        this.territory = territory;
        this.difficulty = difficulty;
        this.name = name;
        this.description = description;
        this.requiredSpells = requiredSpells;
        this.status = MissionStatus.CREATED;
        this.assignments = new HashSet<>();

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
}