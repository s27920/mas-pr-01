package App.Models.Magic;

import App.Models.Mission.Mission;
import App.Types.SpellTypePlusValTuple;
import App.Util.SuperObject;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public class Spell extends SuperObject {
    private final String name;
    private Double damageVal;
    private Double healingVal;
    private Double damageBoostVal;

    private final EnumSet<SpellType> spellTypes;
    private final Set<KnownSpell> knownSpells;
    private final Set<RequiredSpell> missionsWhereRequired;

    private final Set<Mission> missionRequirement;
    public Spell(String name, Set<KnownSpell> knownSpells, SpellTypePlusValTuple... tuples) {
        this.name = name;
        this.knownSpells = knownSpells;
        this.spellTypes = EnumSet.noneOf(SpellType.class);
        for (SpellTypePlusValTuple tuple : tuples) {
            spellTypes.add(tuple.type());
            switch (tuple.type()) {
                case Damage -> setDamageVal(tuple.value());
                case Healing -> setHealingVal(tuple.value());
                case Support -> setDamageBoostVal(tuple.value());
                }
        }
        this.missionRequirement = new HashSet<>();
        missionsWhereRequired = new HashSet<>();
    }

    public Spell(String name) {
        this.name = name;
        this.spellTypes = EnumSet.noneOf(SpellType.class);
        this.knownSpells = new HashSet<>();
        this.missionRequirement = new HashSet<>();
        missionsWhereRequired = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public Double getDamageVal() {
        if (spellTypes.contains(SpellType.Damage)){
            return damageVal;
        }
        throw new IllegalArgumentException("Spell does not deal damage");
    }

    public void setDamageVal(Double damageVal) {
        if (spellTypes.contains(SpellType.Damage)){
            this.damageVal = damageVal;
        }
        throw new IllegalArgumentException("Spell does not deal damage");
    }

    public Double getHealingVal() {
        if (spellTypes.contains(SpellType.Healing)){
            return healingVal;
        }
        throw new IllegalArgumentException("Spell does not heal");
    }

    public void setHealingVal(Double healingVal) {
        if (spellTypes.contains(SpellType.Healing)){
            this.healingVal = healingVal;
        }
        throw new IllegalArgumentException("Spell does not heal");
    }

    public Double getDamageBoostVal() {
        if (spellTypes.contains(SpellType.Support)){
            return damageBoostVal;
        }
        throw new IllegalArgumentException("Spell does give damage boost");
    }

    public void setDamageBoostVal(Double damageBoostVal) {
        if (spellTypes.contains(SpellType.Support)){
            this.damageBoostVal = damageBoostVal;
        }
        throw new IllegalArgumentException("Spell does give damage boost");
    }

    public Set<KnownSpell> getKnownSpells() {
        return Collections.unmodifiableSet(knownSpells);
    }

    public void addKnownSpell(KnownSpell spell){
        knownSpells.add(spell);
    }

    public void addRequiredSpell(RequiredSpell requiredSpell) {
        this.missionsWhereRequired.add(requiredSpell);
    }
}
