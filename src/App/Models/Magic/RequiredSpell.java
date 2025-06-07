package App.Models.Magic;

import App.Models.Mission.Mission;

public class RequiredSpell {
    private final Spell requiredSpell;
    private final Mission mission;
    private int knownLevel;

    public RequiredSpell(Spell requiredSpell, Mission mission) {
        this.requiredSpell = requiredSpell;
        this.mission = mission;
        this.knownLevel = 1;
        requiredSpell.addRequiredSpell(this);
        mission.addMissionRequirement(this);
    }

    public Spell getRequiredSpell() {
        return requiredSpell;
    }

    public Mission getMission() {
        return mission;
    }

    public void levelUp(){
        if(this.knownLevel < 10){
            this.knownLevel++;
        }
    }

    public int getKnownLevel() {
        return knownLevel;
    }
}
