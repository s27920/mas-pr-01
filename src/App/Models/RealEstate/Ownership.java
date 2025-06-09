package App.Models.RealEstate;

import App.Models.Wizard.Wizard;
import App.Util.SuperObject;

import java.time.LocalDate;

public class Ownership extends SuperObject {
    private final LocalDate purchaseDate;
    private Wizard wizard;
    private Domicile domicile;
    public Ownership(LocalDate purchaseDate, Wizard wizard, Domicile domicile) {
        this.purchaseDate = purchaseDate;
        this.wizard = wizard;
        this.domicile = domicile;
        wizard.addToOwned(this);
        domicile.addToOwners(this);
    }

    public void setWizard(Wizard wizard) {
        this.wizard = wizard;
    }

    public void setDomicile(Domicile domicile) {
        this.domicile = domicile;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public Wizard getWizard() {
        return wizard;
    }

    public Domicile getDomicile() {
        return domicile;
    }
}
