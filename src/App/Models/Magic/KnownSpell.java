package App.Models.Magic;

import App.Models.Magic.Spells.Spell;
import App.Util.SuperObject;
import App.Models.Wizard.Wizard;

public class KnownSpell extends SuperObject implements Comparable<KnownSpell> {
    private Spell spell;
    private Wizard wizard;

    private int masteryLevel;

    public KnownSpell(Wizard wizard, Spell spell, int masteryLevel) {
        this.spell = spell;
        this.wizard = wizard;
        this.masteryLevel = masteryLevel;
        spell.addKnownSpell(this);
        wizard.addKnownSpell(this);
    }

    public void setMasteryLevel(int masteryLevel) {
        this.masteryLevel = masteryLevel;
//        wizard.removeFromKnownSpells(this);
//        wizard.addKnownSpell(this);
    }

    public Spell getSpell() {
        return spell;
    }

    public double getSuccessRate(){
        return masteryLevel / 10.0;
    }

    @Override
    public int compareTo(KnownSpell o) {
        return Integer.compare(o.masteryLevel, this.masteryLevel);
    }

    public int getMasteryLevel() {
        return masteryLevel;
    }
}
