package Models.RealEstate;

import Models.Util.SuperObject;

import java.util.Set;

public abstract class Domicile extends SuperObject {

    private String address;
    private Set<Ownership> ownership;

    public Domicile(String address, Set<Ownership> ownership) {
        this.address = address;
        this.ownership = ownership;
    }

    public void addToOwners(Ownership ownership) {
        this.ownership.add(ownership);
    }

}
