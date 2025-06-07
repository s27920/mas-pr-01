package App.Models.RealEstate;

import java.time.LocalDate;

public class House extends Domicile{
    private int bedrooms;
    private boolean garden;

    public House(String address, double size, LocalDate constructionDate, int bedrooms, boolean garden) {
        super(address, size, constructionDate);
        this.bedrooms = bedrooms;
        this.garden = garden;
    }
}
