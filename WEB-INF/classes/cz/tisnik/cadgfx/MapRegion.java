package cz.tisnik.cadgfx;

import java.awt.Polygon;

public class MapRegion
{
    private String id;
    private String name;
    private Polygon polygon;
    private boolean isBuilding;

    public MapRegion()
    {
        super();
    }

    public MapRegion(String id, String name, Polygon polygon, boolean isBuilding)
    {
        super();
        this.id = id;
        this.name = name;
        this.polygon = polygon;
        this.isBuilding = isBuilding;
    }

    public String getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public Polygon getPolygon()
    {
        return this.polygon;
    }

    public boolean isBuilding()
    {
        return this.isBuilding;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setPolygon(Polygon polygon)
    {
        this.polygon = polygon;
    }

    public void setBuilding(boolean isBuilding)
    {
        this.isBuilding = isBuilding;
    }

}
