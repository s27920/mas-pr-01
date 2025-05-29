package Models.Magic;

import Models.Util.SuperObject;
import Models.Wizard;

public class KnownSpell extends SuperObject {
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
}
