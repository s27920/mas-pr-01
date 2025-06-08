package App.Models.RealEstate;

import App.Models.Wizard.Wizard;
import App.Util.SuperObject;

import java.time.LocalDate;

public class Ownership extends SuperObject {
    private LocalDate purchaseDate;
    Wizard wizard;
    Domicile domicile;
    public Ownership(LocalDate purchaseDate, Wizard wizard, Domicile domicile) {
        this.purchaseDate = purchaseDate;
        this.wizard = wizard;
        this.domicile = domicile;
        wizard.addToOwned(this);
        domicile.addToOwners(this);
    }
}
