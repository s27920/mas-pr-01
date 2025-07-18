package App.Models.RealEstate;

import App.Util.SuperObject;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class Domicile extends SuperObject {

    private String address;
    private double size;
    private LocalDate constructionDate;

    private Set<Ownership> ownership;

    public Domicile(String address, double size, LocalDate constructionDate) {
        this.address = address;
        this.size = size;
        this.constructionDate = constructionDate;
        this.ownership = new HashSet<>();
    }


    public void addToOwners(Ownership ownership) {
        this.ownership.add(ownership);
    }

    public Set<Ownership> getOwnership() {
        return Collections.unmodifiableSet(ownership);
    }
}
