package App.Models.Magic.Spells;

import App.Util.SuperObject;

public class DamageSpell extends SuperObject implements ISpellMarker<Double> {
    private double damageVal;

    public DamageSpell(double damageVal) {
        super();
        this.damageVal = damageVal;
    }

    @Override
    public Double getValues() {
        return damageVal;
    }

    public void setDamageVal(double damageVal) {
        this.damageVal = damageVal;
    }

    @Override
    public void removeObj() {
        super.removeObj();
    }
}
