package cz.tisnik.cadgfx;

import java.awt.Polygon;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import cz.tisnik.cadgfx.utils.Log;

public class MapRegions
{
    enum Operation
    {
        NONE,
        MAP,
        REGION,
        POLYGON,
    }

    private static Map<String, List<MapRegion>> regions = new HashMap<String, List<MapRegion>>();

    /**
     * Instance objektu použitého pro logování do logovacího souboru či na
     * standardní výstup servlet kontejneru.
     */
    private static Log log = new Log( MapRegions.class.getName() );

    private static String lineNo$(int lineNo)
    {
        return String.format("$RED$%04d", lineNo);
    }

    public static void readDataFromFile(ServletContext servletContext)
    {
        log.logBegin("readDataFromFile");

        String map = null;
        MapRegion region = null;
        Polygon polygon = null;

        BufferedReader in = null;
        Operation operation = Operation.NONE;
        int lineNo = 0;
        try
        {
            log.logBegin("opening input data stream");
            in = new BufferedReader(new InputStreamReader(servletContext.getResourceAsStream("/data/maps.txt"), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null)
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
                    case MAP:
                        operation = Operation.NONE;
                        log.logEnd(lineNo$(lineNo) + "  $VIOLET$map " + map);
                        break;
                    case REGION:
                        operation = Operation.MAP;
                        MapRegions.regions.get(map).add(region);
                        log.logEnd(lineNo$(lineNo) + "  $VIOLET$region " + region.getId());
                        break;
                    case POLYGON:
                        operation = Operation.REGION;
                        region.setPolygon(polygon);
                        log.logEnd(lineNo$(lineNo) + "  $VIOLET$polygon $GRAY$with " + polygon.npoints + " points");
                        break;
                    }
                }
                else if ("map".equals(key))
                {
                    if (operation == Operation.NONE)
                    {
                        map = value;
                        operation = Operation.MAP;
                        MapRegions.regions.put(map, new ArrayList<MapRegion>());
                        log.logBegin(lineNo$(lineNo) + "  $GREEN$map " + map);
                    }
                }
                else if ("region".equals(key))
                {
                    if (operation == Operation.MAP)
                    {
                        operation = Operation.REGION;
                        region = new MapRegion();
                        log.logBegin(lineNo$(lineNo) + "  $GREEN$region");
                    }
                }
                else if ("id".equals(key))
                {
                    if (operation == Operation.REGION)
                    {
                        log.logSet("id", value);
                        region.setId(value);
                    }
                }
                else if ("name".equals(key))
                {
                    if (operation == Operation.REGION)
                    {
                        log.logSet("name", value);
                        region.setName(value);
                    }
                }
                else if ("type".equals(key))
                {
                    if (operation == Operation.REGION)
                    {
                        log.logSet("building", "building".equals(value));
                        region.setBuilding("building".equals(value));
                    }
                }
                else if ("polygon".equals(key))
                {
                    if (operation == Operation.REGION)
                    {
                        polygon = new Polygon();
                        log.logBegin(lineNo$(lineNo) + "  $GREEN$polygon");
                        operation = Operation.POLYGON;
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
                        log.logSet("point", value);
                    }
                }
            }
        }
        catch (IOException e)
        {
            log.log("$RED$read error $GRAY$: " + e.getMessage());
            e.printStackTrace();
        }
        try
        {
            if (in != null)
            {
                log.logEnd("closing input data stream");
                in.close();
            }
        }
        catch (IOException e)
        {
            log.log("$RED$stream close error $GRAY$: " + e.getMessage());
            e.printStackTrace();
        }
        log.logEnd("readDataFromFile");
    }

    public static List<MapRegion> getMapRegions(String mapName)
    {
        return regions.get(mapName);
    }
}
