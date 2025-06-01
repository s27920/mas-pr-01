package Models.Guild;

import Models.Util.Coords;
import Models.Util.SuperObject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Territory extends SuperObject {
    private String name;
    private Coords coords;

    public static Coords[] pois = {
          new Coords(71, 69),
          new Coords(142, 103),
          new Coords(96, 165),
          new Coords(269, 150),
          new Coords(208, 147),
          new Coords(255, 16),
          new Coords(339, 75),
          new Coords(412, 55),
          new Coords(367, 148),
          new Coords(291, 230),
          new Coords(104, 340),
          new Coords(130, 402),
          new Coords(253, 361),
          new Coords(296, 443),
          new Coords(359, 304),
          new Coords(428, 340),
          new Coords(551, 375),
          new Coords(611, 431),
          new Coords(496, 191),
          new Coords(538, 127),
          new Coords(645, 143),
          new Coords(568, 61),
          new Coords(619, 263)
    };

    public Territory(String name, Coords coords) {
        this.name = name;
        this.coords = coords;
    }

    public static Coords[] getNUniquePois(int n){
        Set<Coords> selectedPois = new HashSet<>();
        for (int i = 0; i < n; i++) {
            Coords poi;
            do {
                poi = pois[((int) (Math.random() * pois.length))];
            } while (selectedPois.contains(poi));
            selectedPois.add(poi);
        }
        return selectedPois.toArray(new Coords[0]);
    }

    public Coords getCoords() {
        return coords;
    }
}
