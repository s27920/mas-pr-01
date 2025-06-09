package App.Models.Wizard;

import App.Models.Magic.KnownSpell;
import App.Models.Magic.Spells.Spell;
import App.Models.Magic.SpellTome;
import App.Models.RealEstate.Domicile;
import App.Models.RealEstate.Ownership;
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


    public void purchaseDomicile(Domicile domicile){
        this.ownedDomiciles.add(new Ownership(LocalDate.now(), this, domicile));
    }

    public void learnSpell(Spell spell){
        this.knownSpells.add(new KnownSpell(this, spell, 1));
    }

    public void addKnownSpell(KnownSpell spell){
        System.out.println("added Known Spell");
        knownSpells.add(spell);
    }

    public void removeFromKnownSpells(KnownSpell spell){
        knownSpells.remove(spell);
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

    public void rebuildSortedTree() {
        SortedSet<KnownSpell> sortedSet = new TreeSet<>(KNOWN_SPELL_COMPARATOR);
        sortedSet.addAll(this.knownSpells);
        this.knownSpells = sortedSet;
    }
}

class KnownSpellComparator implements Comparator<KnownSpell>, Serializable {
    @Override
    public int compare(KnownSpell ks1, KnownSpell ks2) {
        int masteryCompare = Integer.compare(ks2.getMasteryLevel(), ks1.getMasteryLevel());
        if (masteryCompare != 0) return masteryCompare;

        int nameCompare = ks1.getSpell().getName().compareTo(ks2.getSpell().getName());
        if (nameCompare != 0) return nameCompare;

        return Integer.compare(ks1.hashCode(), ks2.hashCode());
    }
}
