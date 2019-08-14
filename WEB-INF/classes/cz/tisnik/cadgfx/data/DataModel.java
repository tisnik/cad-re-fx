package cz.tisnik.cadgfx.data;

import java.util.*;

public class DataModel
{
    private Map<String, Building> buildings;
    private RoomTypes roomTypes;
    private Colors colors;
    private Pozadavky pozadavky;

    public DataModel()
    {
        this.buildings = new HashMap<String, Building>();
    }

    public Colors getColors()
    {
        return colors;
    }

    public void setColors(Colors colors)
    {
        this.colors = colors;
    }

    public RoomTypes getRoomTypes()
    {
        return roomTypes;
    }

    public void setRoomTypes(RoomTypes roomTypes)
    {
        this.roomTypes = roomTypes;
    }

    public void setPozadavky(Pozadavky pozadavky)
    {
        this.pozadavky = pozadavky;
    }

    public Pozadavky getPozadavky()
    {
        return this.pozadavky;
    }

    public void addBuilding(String sapId, Building building)
    {
        this.buildings.put(sapId, building);
    }

    public Building getBuilding(String buildingId)
    {
        return this.buildings.get(buildingId);
    }

    public Map<String, Building> getBuildings()
    {
        return this.buildings;
    }

}
