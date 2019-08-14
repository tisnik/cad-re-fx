package cz.tisnik.cadgfx.data;

import java.awt.Polygon;
import java.io.*;
import java.util.*;
import javax.servlet.*;

import cz.tisnik.cadgfx.gfxentity.GfxArc;
import cz.tisnik.cadgfx.gfxentity.GfxCircle;
import cz.tisnik.cadgfx.gfxentity.GfxEntity;
import cz.tisnik.cadgfx.gfxentity.GfxLine;
import cz.tisnik.cadgfx.gfxentity.GfxText;
import cz.tisnik.cadgfx.utils.Log;

public class DataModelInFiles
{
    enum Operation
    {
        NONE,
        BUILDING,
        FLOOR,
        FLOOR_VARIANT,
        ROOM,
        POLYGON,
    }

    /**
     * Instance objektu použitého pro logování do logovacího souboru či na
     * standardní výstup servlet kontejneru.
     */
    private Log log = new Log( this.getClass().getName() );

    private String lineNo$(int lineNo)
    {
        return String.format("$RED$%04d", lineNo);
    }

    public DataModel readDataModel(ServletContext servletContext, boolean debugInitDataModel, boolean debugFetchValueObject)
    {
        this.log.logBegin("readDataModel");
        DataModel dataModel = new DataModel();

        Building building = null;
        String buildingSapId = null;
        Floor floor = null;
        String floorSapId = null;
        FloorVariant floorVariant = null;
        String floorVariantName = null;
        Room room = null;
        String roomSapId = null;
        Polygon polygon = null;

        BufferedReader in = null;
        Operation operation = Operation.NONE;
        int lineNo = 0;
        try
        {
            this.log.logBegin("opening input data stream");
            in = new BufferedReader(new InputStreamReader(servletContext.getResourceAsStream("/data/data.txt"), "UTF-8"));
            String line;
            while ((line=in.readLine()) != null)
            {
                lineNo++;
                line = line.trim();
                int space = line.indexOf(' ');
                String key = null, value = null;
                if (space > 0)
                {
                    key = line.substring(0, space);
                    value = line.substring(space + 1);
                }
                else
                {
                    key = line;
                }
                if ("end".equals(key))
                {
                    switch (operation)
                    {
                        case BUILDING:
                            operation = Operation.NONE;
                            dataModel.addBuilding(buildingSapId, building);
                            this.log.logEnd(lineNo$(lineNo)+"  $VIOLET$building $GRAY$" + buildingSapId + ", " + building.getName());
                            break;
                        case FLOOR:
                            operation = Operation.BUILDING;
                            building.addFloor(floorSapId, floor);
                            this.log.logEnd(lineNo$(lineNo)+"  $VIOLET$floor $GRAY$" + floorSapId + ", " + floor.getName() + ", " + floor.getArea());
                            break;
                        case FLOOR_VARIANT:
                            operation = Operation.FLOOR;
                            floorVariant.resolveCodebookItems();
                            floor.addFloorVariant(floorVariantName, floorVariant);
                            this.log.logEnd(lineNo$(lineNo)+"  $VIOLET$variant $GRAY$" + floorVariantName + ", " + floorVariant.getDate());
                            break;
                        case ROOM:
                            operation = Operation.FLOOR_VARIANT;
                            floorVariant.addRoom(roomSapId, room);
                            this.log.logEnd(lineNo$(lineNo)+"  $VIOLET$room $GRAY$" + roomSapId );
                            break;
                        case POLYGON:
                            operation = Operation.ROOM;
                            room.addPolygon(polygon);
                            this.log.logEnd(lineNo$(lineNo)+"  $VIOLET$polygon $GRAY$" + polygon.npoints );
                            break;
                    }
                }
                if ("name".equals(key))
                {
                    switch (operation)
                    {
                        case BUILDING:
                            building.setName(value);
                            break;
                        case FLOOR:
                            floor.setName(value);
                            break;
                        case ROOM:
                            room.setName(value);
                            break;
                    }
                }
                if ("pozadavek".equals(key))
                {
                    if (operation == Operation.ROOM)
                    {
                        room.setPozadavek(value);
                    }
                }
                else if ("area".equals(key))
                {
                    switch (operation)
                    {
                        case FLOOR:
                            floor.setArea(Integer.parseInt(value));
                            break;
                        case ROOM:
                            room.setArea(Integer.parseInt(value));
                            break;
                    }
                }
                else if ("type".equals(key))
                {
                    if (operation==Operation.ROOM)
                    {
                        room.setType(Integer.parseInt(value));
                    }
                }
                else if ("capacity".equals(key))
                {
                    if (operation==Operation.ROOM)
                    {
                        room.setCapacity(Integer.parseInt(value));
                    }
                }
                else if ("free".equals(key))
                {
                    if (operation==Operation.ROOM)
                    {
                        room.setFree(Integer.parseInt(value));
                    }
                }
                else if ("date".equals(key))
                {
                    switch (operation)
                    {
                        case FLOOR_VARIANT:
                            floorVariant.setDate(value);
                            break;
                    }
                }
                else if ("building".equals(key))
                {
                    if (operation == Operation.NONE)
                    {
                        operation = Operation.BUILDING;
                        building = new Building();
                        buildingSapId = value;
                        this.log.logBegin(lineNo$(lineNo)+" $GREEN$building $GRAY$" + buildingSapId);
                    }
                }
                else if ("floor".equals(key))
                {
                    if (operation == Operation.BUILDING)
                    {
                        operation = Operation.FLOOR;
                        floor = new Floor();
                        floorSapId = value;
                        this.log.logBegin(lineNo$(lineNo)+"  $GREEN$floor $GRAY$" + floorSapId);
                    }
                }
                else if ("variant".equals(key))
                {
                    if (operation == Operation.FLOOR)
                    {
                        operation = Operation.FLOOR_VARIANT;
                        floorVariant = new FloorVariant();
                        floorVariantName = value;
                        this.log.logBegin(lineNo$(lineNo)+"  $GREEN$variant $GRAY$" + floorVariantName);
                    }
                }
                else if ("room".equals(key))
                {
                    if (operation == Operation.FLOOR_VARIANT)
                    {
                        operation = Operation.ROOM;
                        room = new Room();
                        roomSapId = value;
                        this.log.logBegin(lineNo$(lineNo)+"  $GREEN$room $GRAY$" + roomSapId);
                    }
                }
                else if ("line".equals(key))
                {
                    if (operation == Operation.ROOM)
                    {
                        this.log.log(lineNo$(lineNo)+"    $GREEN$line $GRAY$" + value);
                        room.addGfxEntity(new GfxLine(value));
                    }
                }
                else if ("circle".equals(key))
                {
                    if (operation == Operation.ROOM)
                    {
                        this.log.log(lineNo$(lineNo)+"    $GREEN$circle $GRAY$" + value);
                        room.addGfxEntity(new GfxCircle(value));
                    }
                }
                else if ("arc".equals(key))
                {
                    if (operation == Operation.ROOM)
                    {
                        this.log.log(lineNo$(lineNo)+"    $GREEN$arc $GRAY$" + value);
                        room.addGfxEntity(new GfxArc(value));
                    }
                }
                else if ("text".equals(key))
                {
                    if (operation == Operation.ROOM)
                    {
                        this.log.log(lineNo$(lineNo)+"    $GREEN$text $GRAY$" + value);
                        room.addGfxEntity(new GfxText(value, floor.getDefaultFontSize()));
                    }
                }
                else if ("charterer".equals(key))
                {
                    if (operation == Operation.ROOM)
                    {
                        this.log.log(lineNo$(lineNo)+"    $GREEN$charterer $GRAY$" + value);
                        room.addCharterer(value);
                    }
                }
                else if ("polygon".equals(key))
                {
                    if (operation == Operation.ROOM)
                    {
                        operation = Operation.POLYGON;
                        polygon = new Polygon();
                        this.log.logBegin(lineNo$(lineNo)+"  $GREEN$polygon$GRAY$");
                    }
                }
                else if ("point".equals(key))
                {
                    if (operation == Operation.POLYGON)
                    {
                        String[] values = value.trim().split("\\s+");
                        double x = Double.parseDouble(values[0].trim());
                        double y = Double.parseDouble(values[1].trim());
                        polygon.addPoint((int)Math.round(x), (int)Math.round(y));
                    }
                }
                else if ("scale".equals(key))
                {
                    if (operation == Operation.FLOOR)
                    {
                        this.log.logSet("scale", value);
                        floor.setDefaultScale(Double.parseDouble(value));
                    }
                }
                else if ("xpos".equals(key))
                {
                    if (operation == Operation.FLOOR)
                    {
                        this.log.logSet("x-pos", value);
                        floor.setDefaultXpos(Double.parseDouble(value));
                    }
                }
                else if ("ypos".equals(key))
                {
                    if (operation == Operation.FLOOR)
                    {
                        this.log.logSet("y-pos", value);
                        floor.setDefaultYpos(Double.parseDouble(value));
                    }
                }
                else if ("font_size".equals(key))
                {
                    if (operation == Operation.FLOOR)
                    {
                        this.log.logSet("font_size", value);
                        floor.setDefaultFontSize(Double.parseDouble(value));
                    }
                }
            }
        }
        catch (IOException e)
        {
            this.log.log("$RED$read error $GRAY$: " + e.getMessage());
            e.printStackTrace();
        }
        try
        {
            if (in != null)
            {
                this.log.logEnd("closing input data stream");
                in.close();
            }
        }
        catch (IOException e)
        {
            this.log.log("$RED$stream close error $GRAY$: " + e.getMessage());
            e.printStackTrace();
        }
        this.log.logEnd("readDataModel");
        dataModel.setRoomTypes(new RoomTypes(servletContext));
        dataModel.setColors(new Colors(servletContext));
        dataModel.setPozadavky(new Pozadavky(servletContext));
        return dataModel;
    }

    public void dumpDataModel(ServletContext servletContext, DataModel dataModel)
    {
        this.log.logBegin("dumpDataModel");
        try
        {
            File tempDir = (File)servletContext.getAttribute("javax.servlet.context.tempdir");
            Writer fout = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempDir + "/data.txt"), "UTF-8"));
            try
            {
                dumpBuildings(fout, dataModel);
            }
            finally
            {
                fout.close();
            }
            this.log.log( "check file in directory: " + tempDir.getAbsolutePath() );
        }
        catch (IOException e)
        {
            this.log.logError( e.getMessage() );
        }
        this.log.logEnd("dumpDataModel");
    }

    private void dumpBuildings(Writer fout, DataModel dataModel)
        throws IOException
    {
        for (Map.Entry<String, Building> buildingPair : dataModel.getBuildings().entrySet())
        {
            Building building = buildingPair.getValue();
            fout.write("building " + buildingPair.getKey() + "\n");
            fout.write("    name " + building.getName() + "\n");
            dumpFloors(fout, building);
            fout.write("end\n");
        }
    }

    private void dumpFloors(Writer fout, Building building)
        throws IOException
    {
        for (Map.Entry<String, Floor> floorPair : building.getFloors().entrySet())
        {
            Floor floor = floorPair.getValue();
            fout.write("    floor " + floorPair.getKey() + "\n");
            fout.write("        name " + floor.getName() + "\n");
            fout.write("        area " + floor.getArea() + "\n");
            dumpFloorVariant(fout, floor);
            fout.write("    end\n");
        }
    }

    private void dumpFloorVariant(Writer fout, Floor floor)
        throws IOException
    {
        List<String> keys = new ArrayList<String>(floor.getFloorVariants().keySet());
        Collections.sort(keys);
        for (String key : keys)
        {
            FloorVariant floorVariant = floor.getFloorVariants().get(key);
            fout.write("        variant " + key + "\n");
            fout.write("            date " + floorVariant.getDate() + "\n");
            dumpRooms(fout, floorVariant);
            fout.write("        end\n");
        }
    }

    private void dumpRooms(Writer fout, FloorVariant floorVariant)
        throws IOException
    {
        List<String> keys = new ArrayList<String>(floorVariant.getRooms().keySet());
        Collections.sort(keys);
        for (String key : keys)
        {
            Room room = floorVariant.getRooms().get(key);
            fout.write("            room " + key + "\n");
            fout.write("                name " + room.getName() + "\n");
            if (room.hasTypeSet())
            {
                fout.write("                type " + room.getType() + "\n");
            }
            fout.write("                area " + room.getArea() + "\n");
            if (room.hasCapacitySet())
            {
                fout.write("                capacity " + room.getCapacity() + "\n");
            }
            if (room.isFreeSet())
            {
                fout.write("                free " + room.getFree() + "\n");
            }
            dumpCharterers(fout, room);
            dumpEntitiesOfType(fout, room, GfxText.class.getSimpleName());
            dumpEntitiesOfType(fout, room, GfxLine.class.getSimpleName());
            dumpEntitiesOfType(fout, room, GfxCircle.class.getSimpleName());
            dumpEntitiesOfType(fout, room, GfxArc.class.getSimpleName());
            dumpPolygons(fout, room);
            fout.write("            end\n");
        }
    }

    private void dumpPolygons(Writer fout, Room room) throws IOException
    {
        for (Polygon polygon : room.getPolygons())
        {
            fout.write("                polygon\n");
            for (int i = 0; i < polygon.npoints; i++)
            {
                fout.write("                    point " + polygon.xpoints[i] + " " + polygon.ypoints[i] + "\n");
            }
            fout.write("                end\n");
        }
    }

    private void dumpEntitiesOfType(Writer fout, Room room, String className) throws IOException
    {
        for (GfxEntity entity : room.getEntities())
        {
            if (entity.getClass().getSimpleName().equals(className))
            {
                fout.write("                " + entity.toString() + "\n");
            }
        }
    }

    private void dumpCharterers(Writer fout, Room room) throws IOException
    {
        for (String charterer : room.getCharterers())
        {
            fout.write("                charterer " + charterer + "\n");
        }
    }

}

