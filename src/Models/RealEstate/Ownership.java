package Models.RealEstate;

import Models.Wizard;

import java.time.LocalDate;

public class Ownership {
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
