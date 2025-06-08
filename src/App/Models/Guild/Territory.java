package App.Models.Guild;

import App.Models.Mission.Mission;
import App.Types.Coords;
import App.Util.SuperObject;

import java.util.HashSet;
import java.util.Set;

public class Territory extends SuperObject {
    private String territoryName;
    private int dangerLevel;
    private int population;
    private boolean restrictedAccess;

    private Coords territoryCoordinates;
    private Guild guild;
    private Mission territoryMission;

    public final static Coords[] POIS = {
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
            new Coords(568, 61),
            new Coords(619, 263)
    };

    public Territory(String name, Coords coords, int dangerLevel, boolean restrictedAccess, int population, Guild guild) {
        this.territoryName = name;
        this.territoryCoordinates = coords;
        this.restrictedAccess = restrictedAccess;
        setPopulation(population);
        setDangerLevel(dangerLevel);
        setGuild(guild);
    }

    public static Coords[] getNUniquePois(int n) {
        Set<Coords> selectedPois = new HashSet<>();
        for (int i = 0; i < n; i++) {
            Coords poi;
            do {
                poi = POIS[((int) (Math.random() * POIS.length))];
            } while (selectedPois.contains(poi));
            selectedPois.add(poi);
        }
        return selectedPois.toArray(new Coords[0]);
    }

    public Guild getGuild() {
        return guild;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
        guild.addToOwnedTerritories(this);
    }

    public void setPopulation(int population) {
        if (population >= 0) {
            this.population = population;
        } else {
            throw new IllegalArgumentException("Population must be non-negative");
        }
    }

    public void setDangerLevel(int dangerLevel) {
        if (dangerLevel >= 0 && dangerLevel <= 5) {
            this.dangerLevel = dangerLevel;
        } else {
            throw new IllegalArgumentException("danger level must be [0,5]");
        }
    }

    public Coords getTerritoryCoordinates() {
        return territoryCoordinates;
    }

    public String getTerritoryName() {
        return territoryName;
    }
}
