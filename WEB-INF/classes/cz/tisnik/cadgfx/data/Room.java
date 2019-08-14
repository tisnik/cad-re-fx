package cz.tisnik.cadgfx.data;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

import cz.tisnik.cadgfx.gfxentity.GfxEntity;

public class Room
{
    private String  name;
    private Integer type;
    private Integer area;
    private Integer capacity;
    private Integer free;
    private List<GfxEntity> entities;
    private List<String>    charterers;
    private List<Polygon>   polygons;
    private String  pozadavek;

    public Room()
    {
        this("", null, null, null, null, null);
    }

    public Room(String name, Integer type, Integer area, Integer capacity, Integer free, String pozadavek)
    {
        this.name = name;
        this.type = type;
        this.area = area;
        this.capacity = capacity;
        this.free = free;
        this.entities = new ArrayList<GfxEntity>();
        this.polygons = new ArrayList<Polygon>();
        this.charterers = new ArrayList<String>();
        this.pozadavek = pozadavek;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getType()
    {
        return this.type;
    }

    public boolean hasTypeSet()
    {
        return this.type != null;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getArea()
    {
        return this.area;
    }

    public boolean hasAreaSet()
    {
        return this.area != null;
    }

    public void setArea(Integer area)
    {
        this.area = area;
    }

    public Integer getCapacity()
    {
        return this.capacity;
    }

    public boolean hasCapacitySet()
    {
        return this.capacity != null;
    }

    public void setCapacity(Integer capacity)
    {
        this.capacity = capacity;
    }

    public Integer getFree()
    {
        return this.free;
    }

    public boolean isFreeSet()
    {
        return this.free != null;
    }

    public void setFree(Integer free)
    {
        this.free = free;
    }

    public List<GfxEntity> getEntities()
    {
        return this.entities;
    }

    public List<Polygon> getPolygons()
    {
        return this.polygons;
    }

    public List<String> getCharterers()
    {
        return this.charterers;
    }

    public void addGfxEntity(GfxEntity entity)
    {
        this.entities.add(entity);
    }

    public void addPolygon(Polygon polygon)
    {
        this.polygons.add(polygon);
    }

    public void addCharterer(String charterer)
    {
        this.charterers.add(charterer);
    }

    public boolean hasChartererSet()
    {
        return this.charterers != null;
    }

    public boolean hasCharterer(String charterer)
    {
        return this.charterers.contains(charterer);
    }

    public boolean hasMoreCharterers()
    {
        return this.hasChartererSet() && this.getCharterers().size()>1;
    }

    public String getPozadavek()
    {
        return pozadavek;
    }

    public void setPozadavek(String pozadavek)
    {
        this.pozadavek = pozadavek;
    }
}
