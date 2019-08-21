package cz.tisnik.cadgfx.servlets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cz.tisnik.cadgfx.Configuration;
import cz.tisnik.cadgfx.data.Building;
import cz.tisnik.cadgfx.data.DataModel;
import cz.tisnik.cadgfx.data.Floor;
import cz.tisnik.cadgfx.data.FloorVariant;
import cz.tisnik.cadgfx.data.Room;
import cz.tisnik.cadgfx.gfxentity.GfxEntity;
import cz.tisnik.cadgfx.gfxentity.GfxEntityAttribute;
import cz.tisnik.cadgfx.utils.Log;

public class ImageRenderer extends HttpServlet
{
    private static final Color POLYGON_COLOR = Color.BLACK;

    /**
     * Generated serial version ID.
     */
    private static final long serialVersionUID = 6816217317152901116L;

    private static final double STEP = 16.0;

    private static final Color ColorBrown = new Color(200, 200, 90);
    private static final Color ColorRed = Color.RED;
    private static final Color ColorBlue = Color.BLUE;

    /**
     * Instance objektu použitého pro logování do logovacího souboru či na
     * standardní výstup servlet kontejneru.
     */
    private Log log = new Log( this.getClass().getName() );

    public void init()
    {
        this.log.log("$GREEN$ImageRenderer.init()");
    }

    public void destroy()
    {
        this.log.log("$MAGENTA$ImageRenderer.destroy()");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        this.log.logBegin("doGet()");
        doProcess(request, response);
        this.log.logEnd("doGet()");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        this.log.logBegin("doPost()");
        doProcess(request, response);
        this.log.logEnd("doPost()");
    }

    private void doProcess(HttpServletRequest request, HttpServletResponse response)
        throws IOException
    {
        Configuration configuration = prepareConfiguration(request);
        processParameters(configuration, request, response);
        renderImage(configuration, request, response);
    }

    private Configuration prepareConfiguration(HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        Configuration configuration = (Configuration) session.getAttribute( "configuration" );
        if ( configuration == null )
        {
            DataModel dataModel = (DataModel) this.getServletContext().getAttribute("data");
            configuration = new Configuration(this.getServletContext(), dataModel);
            session.setAttribute( "configuration", configuration );
            this.log.log("$RED$ImageRenderer constructed configuration");
        }
        logConfiguration(configuration);
        return configuration;
    }

    private void logConfiguration(Configuration configuration)
    {
        this.log.log("building: " + configuration.building);
        this.log.log("floor:    " + configuration.floor);
        this.log.log("variant:  " + configuration.floorVariant);
        this.log.log("room:     " + configuration.room);
        this.log.log("scale:    " + configuration.scale);
        this.log.log("x-pos:    " + configuration.xpos);
        this.log.log("y-pos:    " + configuration.ypos);
    }

    private void processParameters(Configuration configuration, HttpServletRequest request, HttpServletResponse response)
    {
        this.log.logBegin("processParameters()");
        HttpSession session = request.getSession();
        ServletContext servletContext = this.getServletContext();
        DataModel dataModel = (DataModel) servletContext.getAttribute("data");
        if (configuration == null)
        {
            configuration = new Configuration(servletContext, dataModel);
            this.log.log("$RED$renderer constructed configuration");
        }
        try
        {
            request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        String command = request.getParameter("command");

        if ("move_left".equals(command))
        {
            configuration.xpos -= STEP/configuration.scale;
        }
        if ("move_right".equals(command))
        {
            configuration.xpos += STEP/configuration.scale;
        }
        if ("move_up".equals(command))
        {
            configuration.ypos -= STEP/configuration.scale;
        }
        if ("move_down".equals(command))
        {
            configuration.ypos += STEP/configuration.scale;
        }
        if ("zoom_plus".equals(command))
        {
            configuration.scale *= 1.2;
        }
        if ("zoom_minus".equals(command))
        {
            configuration.scale /= 1.2;
        }
        if ("zoom_1".equals(command))
        {
            configuration.scale = configuration.defaultScale;
        }
        if ("zoom_all".equals(command))
        {
            configuration.scale = configuration.defaultScale;
            configuration.xpos = configuration.defaultXpos;
            configuration.ypos = configuration.defaultYpos;
        }
        if (request.getParameter("building") != null)
        {
            configuration.selectType = null;
            configuration.building = request.getParameter("building");
            this.log.log("set building to $BLUE$"+configuration.building);
        }
        if (request.getParameter("floor") != null)
        {
            configuration.selectType = null;
            configuration.floor = request.getParameter("floor");
            this.log.log("set floor to $BLUE$"+configuration.floor);
        }
        if (request.getParameter("floorVariant") != null)
        {
            configuration.selectType = null;
            configuration.floorVariant = request.getParameter("floorVariant");
            this.log.log("set floor variant to $BLUE$"+configuration.floorVariant);
        }
        if (request.getParameter("room") != null)
        {
            configuration.selectType = null;
            configuration.room = request.getParameter("room");
            this.log.log("set room to $BLUE$"+configuration.room);
        }
        if (request.getParameter("select") != null)
        {
            configuration.selectType = request.getParameter("select");
            if (configuration.selectType.trim().isEmpty())
            {
                configuration.selectType = null;
            }
            this.log.log("set selectType to $BLUE$"+configuration.selectType);
        }
        if (request.getParameter("changeColor") != null && request.getParameter("status") != null)
        {
            String changeColor = request.getParameter("changeColor");
            String[] splitParam = changeColor.split("_");
            String type = splitParam[0];
            String color = splitParam[1];
            String status = request.getParameter("status");
            if (configuration.disabledColors.get(type) == null)
            {
                configuration.disabledColors.put(type, new HashSet<String>());
            }
            if (status.equals("1"))
            {
                configuration.disabledColors.get(type).remove(color);
            }
            else
            {
                configuration.disabledColors.get(type).add(color);
            }
        }
        if (request.getParameter("coordsx") != null && request.getParameter("coordsy") != null)
        {
            configuration.coordsx = Integer.parseInt(request.getParameter("coordsx"));
            configuration.coordsy = Integer.parseInt(request.getParameter("coordsy"));
            this.log.log("set coords to $BLUE$"+configuration.coordsx+","+configuration.coordsy);
            configuration.room = "";
            double xorg = configuration.imageWidth/2.0;
            double yorg = configuration.imageHeight/2.0;
            double x = configuration.coordsx;
            double y = configuration.coordsy;
            x-=xorg;
            y-=yorg;
            x/=configuration.scale;
            y/=configuration.scale;
            x-=configuration.xpos;
            y-=configuration.ypos;
            x+=xorg;
            y+=yorg;
            Building building = dataModel.getBuilding(configuration.building);
            Floor floor = building == null ? null : building.getFloor(configuration.floor);
            FloorVariant floorVariant = floor == null ? null : floor.getFloorVariant(configuration.floorVariant);
            if (floorVariant != null)
            {
                for (Map.Entry<String, Room> roomPair : floorVariant.getRooms().entrySet())
                {
                    String key = roomPair.getKey();
                    if ("nemaSapID".equals(key)) continue;
                    Room room = roomPair.getValue();
                    for (Polygon polygon : room.getPolygons())
                    {
                        if (polygon.contains(x, y))
                        {
                            configuration.room = key;
                        }
                    }
                }
            }
        }

        session.setAttribute( "configuration", configuration );
        this.log.logEnd("processParameters()");
    }

    private void writeImage(HttpServletResponse response, BufferedImage bi) throws IOException
    {
        response.setHeader( "Pragma", "no-cache" );
        response.addHeader( "Cache-Control", "must-revalidate" );
        response.addHeader( "Cache-Control", "no-cache" );
        response.addHeader( "Cache-Control", "no-store" );
        response.setDateHeader("Expires", 0);
        response.setContentType("image/png;charset=utf-8");
        OutputStream o = response.getOutputStream();
        try
        {
            ImageIO.write(bi, "png", o);
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
        o.flush();
        o.close();
    }

    private void setTransformation(Graphics2D gc, Configuration configuration)
    {
        final double xorg = configuration.imageWidth/2.0;
        final double yorg = configuration.imageHeight/2.0;

        AffineTransform transform = gc.getTransform();
        transform.translate(xorg, yorg);
        transform.scale(configuration.scale, configuration.scale);
        transform.translate((int)configuration.xpos, (int)configuration.ypos);
        transform.translate(-xorg, -yorg);
        gc.setTransform(transform);
    }

    private void renderImage(Configuration configuration, HttpServletRequest request, HttpServletResponse response)
        throws IOException
    {
        this.log.logBegin("renderImage()");
        ServletContext servletContext = this.getServletContext();
        DataModel dataModel = (DataModel) servletContext.getAttribute("data");

        BufferedImage bi = new BufferedImage(configuration.imageWidth, configuration.imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D gc = bi.createGraphics();

        gc.setBackground(Color.WHITE);
        gc.clearRect(0, 0, configuration.imageWidth, configuration.imageHeight);
        gc.setColor(ColorBrown);
        gc.drawRect(0, 0, configuration.imageWidth-1, configuration.imageHeight-1);

        setTransformation(gc, configuration);

        Building building = dataModel.getBuilding(configuration.building);
        Floor floor = building == null ? null : building.getFloor(configuration.floor);
        FloorVariant floorVariant = floor == null ? null : floor.getFloorVariant(configuration.floorVariant);
        Font font = new Font("Helvetica", Font.PLAIN, (int)Math.abs(Math.floor(floor.getDefaultFontSize())));
        gc.setFont(font);

        if (floorVariant != null)
        {
            drawFilledPolygons(configuration, dataModel, gc, floorVariant);
            drawGfxEntities(configuration, dataModel, gc, floor, floorVariant);
            drawPolygons(configuration, dataModel, gc, floor, floorVariant);
            if (configuration.room != null && !"".equals(configuration.room) && configuration.debugSelectedFloor)
            {
                gc.setTransform(new AffineTransform());
                Room room = floorVariant.getRooms().get(configuration.room);
                gc.setColor(Color.WHITE);
                gc.fillRect(1, configuration.imageHeight-42, configuration.imageWidth-2, configuration.imageHeight-2);
                gc.setColor(ColorBlue);
                gc.drawString("Místnost:     " + configuration.room, 10, configuration.imageHeight-22);
                gc.drawString("Název:     " + room.getName(), 10, configuration.imageHeight-6);
                gc.drawString("Plocha:     " + room.getArea(), 200, configuration.imageHeight-22);
                gc.drawString("Kapacita:     " + (room.getCapacity() == null ? "?" : room.getCapacity()), 200, configuration.imageHeight-6);
                gc.setColor(ColorRed);
                int line = 0;
                for (String charterer: room.getCharterers())
                {
                    gc.drawString(charterer, 400, configuration.imageHeight-6-16*line);
                    line ++;
                }
            }
        }

        writeImage(response, bi);
        this.log.log("image written");
        this.log.logEnd("renderImage()");
    }

    private void drawFilledPolygons(Configuration configuration, DataModel dataModel, Graphics2D gc, FloorVariant floorVariant)
    {
        for (Map.Entry<String, Room> roomPair : floorVariant.getRooms().entrySet())
        {
            Room room = roomPair.getValue();
            Color color = resolveFillColor(configuration, dataModel, floorVariant, room);
            if (color != Color.black && color != Color.white)
            {
                gc.setColor(color);
                for (Polygon polygon : room.getPolygons())
                {
                    gc.fillPolygon(polygon);
                }
            }
        }
    }

    private void drawPolygons(Configuration configuration, DataModel dataModel, Graphics2D gc, Floor floor, FloorVariant floorVariant)
    {
        for (Map.Entry<String, Room> roomPair : floorVariant.getRooms().entrySet())
        {
            Room room = roomPair.getValue();
            if (configuration.room != null && roomPair.getKey().equals(configuration.room))
            {
                for (Polygon polygon : room.getPolygons())
                {
                    Color fillColor = resolveFillColor(configuration, dataModel, floorVariant, room);
                    if (fillColor.equals(Color.RED))
                    {
                        gc.setColor(Color.BLUE);
                    }
                    else
                    {
                        gc.setColor(Color.RED);
                    }
                    Stroke oldStroke = gc.getStroke();
                    gc.setStroke(new BasicStroke((float)(4.0/floor.getDefaultScale())));
                    gc.drawPolygon(polygon);
                    gc.setStroke(oldStroke);
                    /*
                    gc.setColor(Color.BLACK);
                    gc.drawPolygon(polygon);
                    */
                }
            }
            else
            {
                gc.setColor(POLYGON_COLOR);
                for (Polygon polygon : room.getPolygons())
                {
                    gc.drawPolygon(polygon);
                }
            }
        }
    }

    private void drawGfxEntities(Configuration configuration, DataModel dataModel, Graphics2D gc,
            Floor floor, FloorVariant floorVariant)
    {
        for (Map.Entry<String, Room> roomPair : floorVariant.getRooms().entrySet())
        {
            Room room = roomPair.getValue();
            if (configuration.room != null && roomPair.getKey().equals(configuration.room))
            {
                gc.setColor(Color.RED);
                for (GfxEntity entity : room.getEntities())
                {
                    entity.draw(gc);
                }
            }
            else
            {
                for (GfxEntity entity : room.getEntities())
                {
                    Color entityColor = resolveEntityColor(entity);
                    gc.setColor(entityColor);
                    if (isEntityWall(entity))
                    {
                        Stroke oldStroke = gc.getStroke();
                        gc.setStroke(new BasicStroke((float)(2.0/floor.getDefaultScale())));
                        entity.draw(gc);
                        gc.setStroke(oldStroke);
                    }
                    else
                    {
                        entity.draw(gc);
                    }
                }
            }
        }
    }

    private Color resolveEntityColor(GfxEntity entity)
    {
        if (entity == null)
        {
            return Color.ORANGE;
        }
        if (entity.getAttribute() == null)
        {
            return Color.YELLOW.darker();
        }
        switch (entity.getAttribute())
        {
        case UNKNOWN:
            return Color.LIGHT_GRAY;
        case DOOR:
            return Color.GREEN;
        case WALL:
            return Color.CYAN.darker();
        case STEP:
            return Color.BLUE;
        case ROOM:
            return Color.PINK;
        default:
            return Color.yellow.darker();
        }
    }

    private boolean isEntityWall(GfxEntity entity)
    {
        return entity.getAttribute() == GfxEntityAttribute.WALL;
    }

    private Color getColor(Color color, Set<String> disabledColors, String colorName)
    {
        return disabledColors == null || !disabledColors.contains(colorName) ? color : Color.white;
    }

    private Color resolveFillColor(Configuration configuration, DataModel dataModel, FloorVariant floorVariant, Room room)
    {
        Color color = Color.BLACK;
        boolean selectRoomType     = "roomtype".equals(configuration.selectType);
        boolean selectArea         = "area".equals(configuration.selectType);
        boolean selectCapacity     = "capacity".equals(configuration.selectType);
        boolean selectOccupation   = "occupation".equals(configuration.selectType);
        boolean selectOwner        = "owner".equals(configuration.selectType);
        boolean selectAvailability = "availability".equals(configuration.selectType);
        boolean selectPozadavek    = "pozadavek".equals(configuration.selectType);

        Set<String> disabledColors = configuration.disabledColors.get(configuration.selectType);

        if (selectRoomType && room.hasTypeSet())
        {
            Integer selectedRoomType = room.getType();
            int index = 0;
            for (Integer roomType : floorVariant.getRoomTypes())
            {
                if (roomType.equals(selectedRoomType))
                {
                    return getColor(dataModel.getColors().getColor(index), disabledColors, selectedRoomType.toString());
                }
                index++;
            }
            return Color.white;
        }
        if (selectPozadavek)
        {
            String pozadavek = room.getPozadavek();
            if (pozadavek == null) pozadavek = "BEZP";
            int index = 0;
            for (String pozad : floorVariant.getRoomPozadavky())
            {
                if (pozad.equals(pozadavek))
                {
                    return getColor(dataModel.getColors().getColor(index), disabledColors, pozadavek);
                }
                index++;
            }
            return Color.white;
        }
        if (selectArea && room.hasAreaSet())
        {
            Integer roomArea = room.getArea();
            if (roomArea == null)
            {
                return Color.white;
            }
            int index = roomArea - roomArea % 10;
            Color clr = getColor(dataModel.getColors().getColor(index/10), disabledColors, Integer.toString(index));
            return clr;
        }
        if (selectCapacity && room.hasCapacitySet())
        {
            int index = 0;
            for (Integer roomCapacity : floorVariant.getRoomCapacities())
            {
                if (roomCapacity.equals(room.getCapacity()))
                {
                    return getColor(dataModel.getColors().getColor(index), disabledColors, Integer.toString(index));
                }
                index++;
            }
            return Color.white;
        }
        if (selectOccupation && room.isFreeSet())
        {
            boolean roomHasInternal = false;
            boolean roomHasExternal = false;
            boolean roomHasCommon = false;
            boolean roomHasFlat = false;
            if (room.hasChartererSet() && room.getCharterers().size()>0)
            {
                for (String roomCharterer : room.getCharterers())
                {
                    switch (roomCharterer.charAt(0))
                    {
                    case 'I': roomHasInternal = true; break;
                    case 'E': roomHasExternal = true; break;
                    case 'C': roomHasCommon = true; break;
                    case 'B': roomHasFlat = true; break;
                    default: break;
                    }
                }
            }
            //boolean roomHasIntExt = roomHasInternal && roomHasExternal;
            if (room.getFree() == 1)
            {
                return getColor(dataModel.getColors().getColor(0), disabledColors, "free");
            }
            if (roomHasInternal)
            {
                return getColor(dataModel.getColors().getColor(2), disabledColors, "internal");
            }
            if (roomHasExternal)
            {
                return getColor(dataModel.getColors().getColor(3), disabledColors, "external");
            }
            if (roomHasFlat)
            {
                return getColor(dataModel.getColors().getColor(4), disabledColors, "flat");
            }
            if (roomHasCommon)
            {
                return getColor(dataModel.getColors().getColor(1), disabledColors, "common");
            }
            return Color.white;
        }
        if (selectOwner && room.hasChartererSet())
        {
            if ("Ostatni".equals(room.getName()))
            {
                return Color.white;
            }
            if (room.hasMoreCharterers())
            {
                Color clr = getColor(dataModel.getColors().getColor(2), disabledColors, "MoreThanOne");
                if (clr != Color.white)
                {
                    return clr;
                }
            }
            // 3 is ok as index 2 is reserved for rooms with more than one charterers
            int index = 3;
            boolean common = false;
            for (String roomCharterer : floorVariant.getRoomCharterers())
            {
                if (!"C Společné".equals(roomCharterer))
                {
                    for (String charterer : room.getCharterers())
                    {
                        if (charterer.equals(roomCharterer))
                        {
                            Color clr = getColor(dataModel.getColors()
                                    .getColor(index), disabledColors, Integer
                                    .toString(index));
                            if (clr != Color.white)
                            {
                                return clr;
                            }
                        }
                    }
                    index++;
                }
            }
            for (String charterer : room.getCharterers())
            {
                if ("C Společné".equals(charterer))
                {
                    common = true;
                }
            }
            if (room.getCharterers().isEmpty())
            {
                Color clr = getColor(dataModel.getColors().getColor(0),
                        disabledColors, "free");
                if (clr != Color.white)
                {
                    return clr;
                }
            }
            if (common)
            {
                Color clr = getColor(dataModel.getColors().getColor(1),
                        disabledColors, "common");
                if (clr != Color.white)
                {
                    return clr;
                }
            }
            return Color.white;
        }
        if (selectAvailability && room.hasCapacitySet() && room.hasChartererSet())
        {
            int index = 0;
            for (Integer roomAvailability : floorVariant.getRoomAvailabilities())
            {
                if (roomAvailability.equals(room.getCapacity()-room.getCharterers().size()))
                {
                    return getColor(dataModel.getColors().getColor(index), disabledColors, Integer.toString(index));
                }
                index++;
            }
            return Color.white;
        }

        return color;
    }

}

