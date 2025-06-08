package App.Models.Magic;

import App.Models.Wizard.Wizard;
import App.Util.SuperObject;

public class MagicResource extends SuperObject {
    private final String resourceName;
    private final String description;
    private long quantity;
    private double unitCost;

    private final Rarity rarity;
    private Wizard wizard;

    public MagicResource(String resourceName, String description, long quantity, double unitCost, Rarity rarity) {
        this.resourceName = resourceName;
        this.description = description;
        this.quantity = quantity;
        this.unitCost = unitCost;
        this.rarity = rarity;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getDescription() {
        return description;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public Wizard getWizard() {
        return wizard;
    }

    public void setWizard(Wizard wizard) {
        this.wizard = wizard;
    }
}
