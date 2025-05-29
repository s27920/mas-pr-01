package Models.Guild;

import Models.Util.Coords;
import Models.Util.SuperObject;

public class Territory extends SuperObject {
    String name;
    Coords coords;

    public Territory(String name, Coords coords) {
        this.name = name;
        this.coords = coords;
    }

    public Coords getCoords() {
        return coords;
    }
}
