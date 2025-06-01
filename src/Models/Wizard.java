package Models;

import Models.Magic.KnownSpell;
import Models.Magic.Spell;
import Models.Magic.SpellTome;
import Models.RealEstate.Domicile;
import Models.RealEstate.Ownership;
import Models.Util.SuperObject;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Wizard extends SuperObject {
    private String name;
    private Set<KnownSpell> knownSpells;
    private Set<Ownership> ownedDomiciles;
    private Set<SpellTome> ownedTomes;
    private int chosenIcon;


    public void purchaseDomicile(Domicile domicile){
        this.ownedDomiciles.add(new Ownership(LocalDate.now(), this, domicile));
    }

    public void learnSpell(Spell spell){
        knownSpells.add(new KnownSpell(this, spell, 1));
    }

    public void addKnownSpell(KnownSpell spell){
        knownSpells.add(spell);
    }

    public void addToOwned(Ownership ownership) {

    }

    public Set<KnownSpell> getKnownSpells() {
        return knownSpells;
    }

    public void addTome(SpellTome tome){
        this.ownedTomes.add(tome);
        tome.setWizard(this);
    }

    public void LearnSpell(Spell spell){
        this.knownSpells.add(new KnownSpell(this, spell, 1));
    }


    public int getChosenIcon() {
        return chosenIcon;
    }

    public Wizard(String name, int chosenIcon) {
        this.name = name;
        this.chosenIcon = chosenIcon;
        this.knownSpells = new HashSet<>();
        this.ownedDomiciles = new HashSet<>();
        this.ownedTomes = new HashSet<>();
    }

    public Wizard(String name) {
        this.name = name;
        this.chosenIcon = 1;
        this.knownSpells = new HashSet<>();
        this.ownedDomiciles = new HashSet<>();
        this.ownedTomes = new HashSet<>();
    }

    public Wizard(String name, Set<KnownSpell> knownSpells, Set<Ownership> ownedDomiciles, Set<SpellTome> ownedTomes) {
        this.name = name;
        this.knownSpells = knownSpells;
        this.ownedDomiciles = ownedDomiciles;
        this.ownedTomes = ownedTomes;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Wizard{");
        sb.append("name='").append(name).append('\'');
        sb.append(", knownSpells=").append(knownSpells);
        sb.append(", ownedDomiciles=").append(ownedDomiciles);
        sb.append(", ownedTomes=").append(ownedTomes);
        sb.append(", chosenIcon=").append(chosenIcon);
        sb.append('}');
        return sb.toString();
    }
}
