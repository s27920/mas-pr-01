package App.Models.Magic.Spells;

import App.Util.SuperObject;

public class HealingSpell extends SuperObject implements ISpellMarker<Double> {
    private double healingVal;

    public HealingSpell(double healingVal) {
        super();
        this.healingVal = healingVal;
    }

    @Override
    public Double getValues() {
        return healingVal;
    }

    public void setHealingVal(double healingVal) {
        this.healingVal = healingVal;
    }

    @Override
    public void removeObj() {
        super.removeObj();
    }
}
