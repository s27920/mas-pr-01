package App.Models.Magic.Spells;

import App.Models.Magic.KnownSpell;
import App.Models.Magic.RequiredSpell;
import App.Util.SuperObject;

import java.util.*;

public class Spell extends SuperObject {
    private final String name;
    private double range;
    private final Set<KnownSpell> knownSpells;
    private final Set<RequiredSpell> missionsWhereRequired;
    private final HashMap<Class<?>, ISpellMarker<?>> spellValueStorage;

    public Spell(String name, Set<KnownSpell> knownSpells, HashSet<RequiredSpell> missionsWhereIsRequired, ISpellMarker<?>... passedSpells) {
        super();
        this.name = name;
        this.knownSpells = (knownSpells != null) ? knownSpells : new HashSet<>();
        this.missionsWhereRequired = (missionsWhereIsRequired != null) ? missionsWhereIsRequired : new HashSet<>();
        this.spellValueStorage = new HashMap<>();

        for (ISpellMarker<?> spell : passedSpells) {
            spellValueStorage.put(spell.getClass(), spell);
        }
    }

    @Override
    public void removeObj() {
        ISpellMarker<?>[] spellsPartIncluded = spellValueStorage.values().toArray(new ISpellMarker<?>[0]);
        for (int i = spellsPartIncluded.length - 1; i >= 0; i--) {
            spellsPartIncluded[i].removeObj();
        }
        spellValueStorage.clear();
        super.removeObj();
    }

    public <T> T getValue(Class<?> clazz) throws Exception {
        ISpellMarker<?> marker = spellValueStorage.get(clazz);
        if (marker == null) {
            throw new Exception("Spell is not of type : " + clazz.getSimpleName());
        }
        return (T) marker.getValues();
    }

    public boolean hasSpecialization(Class<?> clazz) {
        return spellValueStorage.containsKey(clazz);
    }

    public void addDamageSpecialization(double damageVal) {
        spellValueStorage.put(DamageSpell.class, new DamageSpell(damageVal));
    }

    public void addHealingSpecialization(double healingVal) {
        spellValueStorage.put(HealingSpell.class, new HealingSpell(healingVal));
    }

    public void addSupportSpecialization(String[] effects) {
        spellValueStorage.put(SupportSpell.class, new SupportSpell(effects));
    }

    public Double getDamageValue() throws Exception {
        return getValue(DamageSpell.class);
    }

    public Double getHealingValue() throws Exception {
        return getValue(HealingSpell.class);
    }

    public String[] getSupportEffects() throws Exception {
        return getValue(SupportSpell.class);
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        if (range > 0){
            this.range = range;
        }
    }

    public boolean isDamageSpell() {
        return hasSpecialization(DamageSpell.class);
    }

    public boolean isHealingSpell() {
        return hasSpecialization(HealingSpell.class);
    }

    public boolean isSupportSpell() {
        return hasSpecialization(SupportSpell.class);
    }

    public String getName() {
        return name;
    }

    public Set<KnownSpell> getKnownSpells() {
        return Collections.unmodifiableSet(knownSpells);
    }

    public Set<RequiredSpell> getMissionsWhereRequired() {
        return Collections.unmodifiableSet(missionsWhereRequired);
    }

    public void addKnownSpell(KnownSpell spell) {
        knownSpells.add(spell);
    }

    public void addRequiredSpell(RequiredSpell requiredSpell) {
        this.missionsWhereRequired.add(requiredSpell);
    }

    public Set<Class<?>> getSpecializations() {
        return Collections.unmodifiableSet(spellValueStorage.keySet());
    }
}

