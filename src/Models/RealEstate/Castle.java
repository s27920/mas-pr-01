package Models.RealEstate;

import java.util.Set;

public class Castle extends Domicile{
    private double squareFootage;

    public Castle(String address, Set<Ownership> ownership, double squareFootage) {
        super(address, ownership);
        this.squareFootage = squareFootage;
    }
}
