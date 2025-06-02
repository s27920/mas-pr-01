package Models.RealEstate;

import Models.Guild.Guild;

import java.time.LocalDate;
import java.util.Set;

public class Castle extends Domicile{
    private int towers;
    private int dungeons;
    private boolean throneRoom;
    private boolean library;
    private int defenceRating;
    private int siegeCapacity;

    private Guild ownsCastle;

    public Castle(String address, double size, LocalDate constructionDate, int towers, int dungeons, boolean throneRoom, boolean library, int defenceRating, int siegeCapacity) {
        super(address, size, constructionDate);
        this.towers = towers;
        this.dungeons = dungeons;
        this.throneRoom = throneRoom;
        this.library = library;
        this.defenceRating = defenceRating;
        this.siegeCapacity = siegeCapacity;
    }

    public Guild getOwnsCastle() {
        return ownsCastle;
    }

    public void setOwnsCastle(Guild ownsCastle) {
        this.ownsCastle = ownsCastle;
    }
}
