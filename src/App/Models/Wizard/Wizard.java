package App.Models.Wizard;

import App.Models.Magic.KnownSpell;
import App.Models.Magic.Spells.Spell;
import App.Models.Magic.SpellTome;
import App.Models.RealEstate.Domicile;
import App.Models.RealEstate.Ownership;
import App.Util.KnownSpellComparator;
import App.Util.SuperObject;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

public class Wizard extends SuperObject {
    private String name;
    private SortedSet<KnownSpell> knownSpells;
    private Set<Ownership> ownedDomiciles;
    private Set<SpellTome> ownedTomes;
    private int chosenIcon;

    private static final Comparator<KnownSpell> KNOWN_SPELL_COMPARATOR = new KnownSpellComparator();


    public Wizard(String name, int chosenIcon) {
        this.name = name;
        this.chosenIcon = chosenIcon;
        this.knownSpells = new TreeSet<>(KNOWN_SPELL_COMPARATOR);
        this.ownedDomiciles = new HashSet<>();
        this.ownedTomes = new HashSet<>();
    }

    public boolean removeFromKnownSpells(KnownSpell knownSpell){
        return this.knownSpells.remove(knownSpell);
    }
    public boolean addToKnownSpells(KnownSpell knownSpell){
        return this.knownSpells.add(knownSpell);
    }


    public void purchaseDomicile(Domicile domicile){
        this.ownedDomiciles.add(new Ownership(LocalDate.now(), this, domicile));
    }

    public KnownSpell learnSpell(Spell spell){
        KnownSpell knownSpell = new KnownSpell(this, spell, 1);
        this.knownSpells.add(knownSpell);
        return knownSpell;
    }

    public void addKnownSpell(KnownSpell spell){
        knownSpells.add(spell);
    }


    public void addToOwned(Ownership ownership) {

    }

    public Set<KnownSpell> getKnownSpells() {
        return Collections.unmodifiableSet(knownSpells);
    }

    public void addTome(SpellTome tome){
        this.ownedTomes.add(tome);
        tome.setOwnedBy(this);
    }

    public void LearnSpell(Spell spell){
        this.knownSpells.add(new KnownSpell(this, spell, 1));
    }

    public int getChosenIcon() {
        return chosenIcon;
    }

    public String getName() {
        return name;
    }

}

