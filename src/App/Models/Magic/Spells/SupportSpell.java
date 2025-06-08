package App.Models.Magic.Spells;

import App.Util.SuperObject;

public class SupportSpell extends SuperObject implements ISpellMarker<String[]> {
    private String[] effects;

    public SupportSpell(String... effects){
        super();
        this.effects = effects != null ? effects.clone() : new String[0];
    }

    @Override
    public String[] getValues() {
        return effects.clone();
    }

    public void setEffects(String[] effects) {
        this.effects = effects != null ? effects.clone() : new String[0];
    }

    @Override
    public void removeObj() {
        super.removeObj();
    }
}
