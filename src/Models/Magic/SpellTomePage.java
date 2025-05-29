package Models.Magic;

import Models.Util.SuperObject;

public class SpellTomePage extends SuperObject {
    private SpellTome spellTome;
    private String text;

    public SpellTomePage(SpellTome spellTome, String text) {
        this.spellTome = spellTome;
        this.text = text;
    }

    public void setSpellTome(SpellTome spellTome) {
        this.spellTome = spellTome;
        spellTome.addPage(this);
    }
}
