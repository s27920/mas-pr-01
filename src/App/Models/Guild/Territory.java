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



    public Territory(String name, Coords coords, int dangerLevel, boolean restrictedAccess, int population, Guild guild) {
        this.territoryName = name;
        this.territoryCoordinates = coords;
        this.restrictedAccess = restrictedAccess;
        setPopulation(population);
        setDangerLevel(dangerLevel);
        setGuild(guild);
    }

    public int getDangerLevel() {
        return dangerLevel;
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
