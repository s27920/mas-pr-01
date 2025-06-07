package App.Models.Wizard;

import App.Models.Magic.KnownSpell;
import App.Models.Magic.Spell;
import App.Models.Magic.SpellTome;
import App.Models.RealEstate.Domicile;
import App.Models.RealEstate.Ownership;
import App.Util.SuperObject;

import java.time.LocalDate;
import java.util.*;

public class Wizard extends SuperObject {
    private String name;
    private SortedSet<KnownSpell> knownSpells;
    private Set<Ownership> ownedDomiciles;
    private Set<SpellTome> ownedTomes;
    private int chosenIcon;

    public Wizard(String name, int chosenIcon) {
        this.name = name;
        this.chosenIcon = chosenIcon;
        this.knownSpells = new TreeSet<>(
                Comparator.comparingInt(KnownSpell::getMasteryLevel)
                        .reversed()
                        .thenComparing(ks -> ks.getSpell().getName())
        );
        this.ownedDomiciles = new HashSet<>();
        this.ownedTomes = new HashSet<>();
    }

    public Wizard(String name, SortedSet<KnownSpell> knownSpells, Set<Ownership> ownedDomiciles, Set<SpellTome> ownedTomes, int chosenIcon) {
        this.name = name;
        this.knownSpells = knownSpells;
        this.ownedDomiciles = ownedDomiciles;
        this.ownedTomes = ownedTomes;
        this.chosenIcon = chosenIcon;
    }

    public void purchaseDomicile(Domicile domicile){
        this.ownedDomiciles.add(new Ownership(LocalDate.now(), this, domicile));
    }

    public void learnSpell(Spell spell){
        this.knownSpells.add(new KnownSpell(this, spell, 1));
    }

    public void addKnownSpell(KnownSpell spell){
        knownSpells.add(spell);
    }
    public void removeFromKnownSpells(KnownSpell spell){
        knownSpells.remove(spell);
    }

    public void addToOwned(Ownership ownership) {

    }

    public Set<KnownSpell> getKnownSpells() {
        return knownSpells;
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

    public void rebuildSortedTree() {
        SortedSet<KnownSpell> sortedSet = new TreeSet<>();
        sortedSet.addAll(this.knownSpells);
        this.knownSpells = sortedSet;
    }
}
