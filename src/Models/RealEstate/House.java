package Models.RealEstate;

import java.util.Set;

public class House extends Domicile{
    private int chimneyCount;

    public House(String address, Set<Ownership> ownership, int chimneyCount) {
        super(address, ownership);
        this.chimneyCount = chimneyCount;
    }

}
