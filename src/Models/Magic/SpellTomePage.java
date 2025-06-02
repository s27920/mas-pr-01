package Models.Magic;

import Models.Util.SuperObject;

public class SpellTomePage extends SuperObject {
    private SpellTome spellTome;
    private int pageNumber;
    private String text;
    private String usedInRituals;

    public SpellTomePage(int pageNumber, String text, String usedInRituals) {
        this.pageNumber = pageNumber;
        this.text = text;
        this.usedInRituals = usedInRituals;
    }

    public void setSpellTome(SpellTome spellTome) {
        this.spellTome = spellTome;
        spellTome.addPage(this);
    }
}
