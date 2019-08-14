package cz.tisnik.cadgfx.data;

import java.util.*;

public class FloorVariant
{
    private String date;
    private Map<String, Room> rooms;
    private Set<Integer> roomTypes;
    private Set<Integer> roomAreas;
    private Set<Integer> roomCapacities;
    private Set<String>  roomCharterers;
    private Set<Integer> roomAvailabilities;
    private Set<String>  roomPozadavky;

    public FloorVariant()
    {
        this.rooms = new HashMap<String, Room>();
        this.roomTypes = new TreeSet<Integer>();
        this.roomAreas = new TreeSet<Integer>();
        this.roomCapacities = new TreeSet<Integer>();
        this.roomCharterers = new TreeSet<String>();
        this.roomAvailabilities = new TreeSet<Integer>();
        this.roomPozadavky = new TreeSet<String>();
    }

    public FloorVariant(String date)
    {
        this();
        this.date = date;
    }

    public void resolveCodebookItems()
    {
        this.roomTypes.clear();
        this.roomAreas.clear();
        this.roomCapacities.clear();
        this.roomCharterers.clear();
        this.roomAvailabilities.clear();
        this.roomPozadavky.clear();
        this.roomPozadavky.add("BEZP");
        for (Room room : this.rooms.values())
        {
            if (room.getType() != null)
            {
                this.roomTypes.add(room.getType());
            }
            if (room.getArea() != null)
            {
                this.roomAreas.add(room.getArea());
            }
            if (room.getCapacity() != null)
            {
                this.roomCapacities.add(room.getCapacity());
            }
            if (room.getCharterers() != null)
            {
                this.roomCharterers.addAll(room.getCharterers());
            }
            if (room.getCapacity() != null && room.getCharterers() != null)
            {
                this.roomAvailabilities.add(room.getCapacity() - room.getCharterers().size());
            }
            if (room.getPozadavek() != null)
            {
                this.roomPozadavky.add(room.getPozadavek());
            }
        }
    }

    public void addRoom(String sapId, Room room)
    {
        this.rooms.put(sapId, room);
    }

    public String getDate()
    {
        return this.date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public Map<String, Room> getRooms()
    {
        return this.rooms;
    }

    public Set<Integer> getRoomTypes()
    {
        return this.roomTypes;
    }

    public Set<Integer> getRoomAreas()
    {
        return this.roomAreas;
    }

    public Set<Integer> getRoomCapacities()
    {
        return this.roomCapacities;
    }

    public Set<String> getRoomCharterers()
    {
        return this.roomCharterers;
    }

    public Set<Integer> getRoomAvailabilities()
    {
        return this.roomAvailabilities;
    }

    public Set<String> getRoomPozadavky()
    {
        return this.roomPozadavky;
    }

}
