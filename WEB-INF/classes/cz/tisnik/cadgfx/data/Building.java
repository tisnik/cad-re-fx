package cz.tisnik.cadgfx.data;

import java.util.*;

public class Building
{
    private String name;
    private Map<String, Floor> floors;

    public Building()
    {
        this.floors = new HashMap<String, Floor>();
    }

    public Building(String name)
    {
        this();
        this.name = name;
    }

    public void addFloor(String sapId, Floor floor)
    {
        this.floors.put(sapId, floor);
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public Floor getFloor(String id)
    {
        return this.floors.get(id);
    }

    public Map<String, Floor> getFloors()
    {
        return this.floors;
    }

}

