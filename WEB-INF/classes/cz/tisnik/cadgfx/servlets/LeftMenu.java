package cz.tisnik.cadgfx.servlets;

import java.awt.Color;
import java.io.IOException;
import java.util.Collection;

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
import cz.tisnik.cadgfx.data.Pozadavky;
import cz.tisnik.cadgfx.data.Room;
import cz.tisnik.cadgfx.data.RoomTypes;
import cz.tisnik.cadgfx.utils.Log;
import cz.tisnik.cadgfx.utils.WebAppCommon;

public class LeftMenu extends HttpServlet
{
    /**
     * Generated serial version ID.
     */
    private static final long serialVersionUID = -5368632750955686427L;

    /**
     * Instance objektu použitého pro logování do logovacího souboru či na
     * standardní výstup servlet kontejneru.
     */
    private Log log = new Log( this.getClass().getName() );

    public void init()
    {
        this.log.log("LeftMenu.init()");
    }

    public void destroy()
    {
        this.log.log("LeftMenu.destroy()");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        this.log.logBegin("doGet()");
        doProcess(request, response);
        this.log.logEnd("doGet()");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        this.log.logBegin("doPost()");
        doProcess(request, response);
        this.log.logEnd("doPost()");
    }

    public void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        this.log.logBegin("doProcess()");
        ServletContext servletContext = this.getServletContext();
        HttpSession session = request.getSession();
        Configuration configuration = (Configuration) session.getAttribute( "configuration" );
        DataModel dataModel = (DataModel) servletContext.getAttribute("data");
        if (configuration == null)
        {
            configuration = new Configuration(servletContext, dataModel);
            System.out.println("dispatcher constructed configuration");
        }
        Building building = dataModel.getBuilding(configuration.building);
        Floor floor = building.getFloor(configuration.floor);
        FloorVariant floorVariant = floor.getFloorVariant(configuration.floorVariant);

        storeTree(request, dataModel, configuration, floorVariant);
        storeDebugInfo(request, floorVariant, servletContext);
        storeAdminCommands(request, servletContext);

        servletContext.getRequestDispatcher( "/jsp/leftmenu.jsp" ).forward( request, response );
        this.log.logEnd("doProcess()");
    }

    private void storeTree(HttpServletRequest request, DataModel dataModel, Configuration configuration,
            FloorVariant floorVariant)
    {
        String path = request.getRequestURI();
        path = path.substring(0, 1+path.indexOf('/', 1));
        request.setAttribute("tree", renderTree(dataModel, configuration, floorVariant, path));
    }

    private void storeDebugInfo(HttpServletRequest request, FloorVariant floorVariant, ServletContext servletContext)
    {
        request.setAttribute("attributes", renderDebugInfo(floorVariant, servletContext));
    }

    private void storeAdminCommands(HttpServletRequest request, ServletContext servletContext)
    {
        request.setAttribute("admin_commands", renderAdminCommands(servletContext));
    }

    private String renderAdminCommands(ServletContext servletContext)
    {
        if (WebAppCommon.areAdminCommandsEnabled(servletContext))
        {
            return "<hr/><a href='ReloadDataModel'>reload data model</a>";
        }
        else
        {
            return "";
        }
    }

    private String renderDebugInfo(FloorVariant floorVariant, ServletContext servletContext)
    {
        if (WebAppCommon.isDisplayFloorAttributesEnabled(servletContext))
        {
            StringBuffer out = new StringBuffer("<hr>");
            for (Integer roomType : floorVariant.getRoomTypes())
            {
                out.append("RT: " + roomType + "<br>");
            }
            for (Integer roomArea : floorVariant.getRoomAreas())
            {
                out.append("AR: " + roomArea + "<br>");
            }
            for (Integer roomCapacity : floorVariant.getRoomCapacities())
            {
                out.append("CA: " + roomCapacity + "<br>");
            }
            for (String roomCharterer : floorVariant.getRoomCharterers())
            {
                out.append("CH: " + roomCharterer + "<br>");
            }
            return out.toString();
        }
        else
        {
            return "";
        }
    }

    private String selectionHeader(String path, String selectionType, String text, Configuration configuration, boolean empty)
    {
        boolean selected = configuration != null && selectionType.equals(configuration.selectType);
        String imageName = selected ? "checkbox_on" : "checkbox_off";
        if (empty)
        {
            imageName = "checkbox_disabled";
        }
        return empty ?
               "<li>"+
               "<img id='checkbox_"+selectionType+"' src='"+path+"img/"+imageName+".png' border='0'/><span style='color:#808080'>"+text+"</span>\n"+
               "    <ul>\n" :
               "<li><a href='' onclick='return selectObjects(\""+selectionType+"\")' >"+
               "<img id='checkbox_"+selectionType+"' src='"+path+"img/"+imageName+".png' border='0'/>"+text+"</a>\n"+
               "    <ul>\n";
    }

    private String endSelectionHeader()
    {
        return "    </ul>\n"+
               "</li>";
    }

    private String item(String path, String color, String value, String text, Configuration configuration, String selectionType)
    {
        boolean selected = true;
        /*
        System.out.println("s " + selectionType);
        System.out.println("c " + color);
        if (selected && configuration != null)
        {
            if (configuration.disabledColors != null && configuration.disabledColors.get(selectionType)!=null)
            {
                selected = !configuration.disabledColors.get(selectionType).contains(color);
            }
        }*/
        String imageName = selected ? "checkbox_on" : "checkbox_off";
        return "<li>"+
               "<a href='' onclick='colorClick(\""+value+"\"); return false;'>"+
               "<img src='"+path+"img/color_"+color+".png' border='0'/>"+
               "<img id='"+value+"' src='"+path+"img/"+imageName+".png' border='0'/>"+text+"</a></li>\n";
    }

    private String disableItem(String path, String color, String value, String text)
    {
        return "<li>"+
        "<span style='color:#404040'>"+
        "<img src='"+path+"img/color_"+color+".png' border='0'/>"+
        "<img id='"+value+"' src='"+path+"img/checkbox_disabled.png' border='0'/>"+text+"</span></li>\n";
    }

    private String renderTree(DataModel dataModel, Configuration configuration, FloorVariant floorVariant, String path)
    {
        StringBuffer out = new StringBuffer();

        out.append(selectionHeader(path, "roomtype", "Druh místnosti", configuration, isRoomTypesEmpty(dataModel, floorVariant)));
        out.append(renderRoomTypes(dataModel, floorVariant, path, configuration, "roomtype"));
        out.append(endSelectionHeader());

        out.append(selectionHeader(path, "area", "Výměra", configuration, isRoomAreasEmpty(dataModel, floorVariant)));
        out.append(renderRoomAreas(dataModel, floorVariant, path, configuration, "area"));
        out.append(endSelectionHeader());

        out.append(selectionHeader(path, "capacity", "Plán.&nbsp;kapacita", configuration, isRoomCapacitiesEmpty(dataModel, floorVariant)));
        out.append(renderRoomCapacities(dataModel, floorVariant, path, configuration, "capacity"));
        out.append(endSelectionHeader());

        out.append(selectionHeader(path, "occupation", "Obsazenost", configuration, isRoomOccupationEmpty(dataModel, floorVariant)));
        out.append(renderRoomOccupation(dataModel, floorVariant, path, configuration, "occupation"));
        out.append(endSelectionHeader());

        out.append(selectionHeader(path, "owner", "Nájemce", configuration, isCharterersEmpty(dataModel, floorVariant)));
        out.append(renderCharterer(dataModel, floorVariant, path, configuration, "owner"));
        out.append(endSelectionHeader());

        out.append(selectionHeader(path, "availability", "Volná&nbsp;kapacita", configuration, isRoomAvailabilitiesEmpty(dataModel, floorVariant)));
        out.append(renderRoomAvailabilities(dataModel, floorVariant, path, configuration, "availability"));
        out.append(endSelectionHeader());

        out.append(selectionHeader(path, "pozadavek", "Požadavky na opravu", configuration, isPozadavkyEmpty(dataModel, floorVariant)));
        out.append(renderRoomPozadavky(dataModel, floorVariant, path, configuration, "pozadavek"));
        out.append(endSelectionHeader());

        return out.toString();
    }

    @SuppressWarnings("unchecked")
    private boolean isCollectionEmpty(Collection collection)
    {
        return collection == null || collection.isEmpty();
    }

    private boolean isRoomTypesEmpty(DataModel dataModel, FloorVariant floorVariant)
    {
        return isCollectionEmpty(floorVariant.getRoomTypes());
    }

    private boolean isRoomAreasEmpty(DataModel dataModel, FloorVariant floorVariant)
    {
        return isCollectionEmpty(floorVariant.getRoomAreas());
    }

    private boolean isRoomCapacitiesEmpty(DataModel dataModel, FloorVariant floorVariant)
    {
        return isCollectionEmpty(floorVariant.getRoomCapacities());
    }

    private boolean isRoomOccupationEmpty(DataModel dataModel, FloorVariant floorVariant)
    {
        // TODO - is it really correct?
        return isCollectionEmpty(floorVariant.getRoomCharterers());
    }

    private boolean isCharterersEmpty(DataModel dataModel, FloorVariant floorVariant)
    {
        return isCollectionEmpty(floorVariant.getRoomCharterers());
    }

    private boolean isRoomAvailabilitiesEmpty(DataModel dataModel, FloorVariant floorVariant)
    {
        return isCollectionEmpty(floorVariant.getRoomAvailabilities());
    }

    private boolean isPozadavkyEmpty(DataModel dataModel, FloorVariant floorVariant)
    {
        return isCollectionEmpty(floorVariant.getRoomPozadavky());
    }

    private String getColor(DataModel dataModel, int index)
    {
        Color color = dataModel.getColors().getColor(index);
        return String.format("%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    private String renderRoomTypes(DataModel dataModel, FloorVariant floorVariant, String path, Configuration configuration, String selectionType)
    {
        StringBuffer out = new StringBuffer();
        RoomTypes roomTypes = dataModel.getRoomTypes();
        int index = 0;
        for (Integer roomTypeId : floorVariant.getRoomTypes())
        {
            String color = getColor(dataModel, index);
            out.append(item(path, color, "roomtype_"+roomTypeId, roomTypes.getRoomType(roomTypeId), configuration, selectionType));
            index++;
        }
        return out.toString();
    }

    private String renderRoomPozadavky(DataModel dataModel, FloorVariant floorVariant, String path, Configuration configuration, String selectionType)
    {
        StringBuffer out = new StringBuffer();
        Pozadavky pozadavky = dataModel.getPozadavky();
        int index = 0;
        for (String pozadavekId : floorVariant.getRoomPozadavky())
        {
            String color = getColor(dataModel, index);
            out.append(item(path, color, "pozadavek_"+pozadavekId, pozadavky.getPozadavek(pozadavekId), configuration, selectionType));
            index++;
        }
        return out.toString();
    }

    private String renderRoomAreas(DataModel dataModel, FloorVariant floorVariant, String path, Configuration configuration, String selectionType)
    {
        StringBuffer out = new StringBuffer();
        int maxArea = 0;
        for (Integer roomArea : floorVariant.getRoomAreas())
        {
            if (maxArea < roomArea)
            {
                maxArea = roomArea;
            }
        }
        if (maxArea > 120)
        {
            maxArea = 120;
        }
        int index = 0;
        for (int area = 0; area <= maxArea; area += 10)
        {
            boolean areaExist = hasAnyRoomArea(floorVariant, area);
            String color = getColor(dataModel, index);
            if (areaExist)
            {
                out.append(item(path, color, "area_" + area, String.format("%d-%d", area, area + 10) + "m<sup>2</sup>", configuration, selectionType));
            }
            else
            {
                out.append(disableItem(path, color, "area_" + area, String.format("%d-%d", area, area + 10) + "m<sup>2</sup>"));
            }
            index++;
        }
        return out.toString();
    }

    private boolean hasAnyRoomArea(FloorVariant floorVariant, int area)
    {
        for (Integer roomArea : floorVariant.getRoomAreas())
        {
            if (roomArea>=area && roomArea<area+10)
            {
                return true;
            }
        }
        return false;
    }

    private String renderCapacityForOneRoom(int capacity)
    {
        if (capacity == 1)
        {
            return "osoba";
        }
        else if (capacity <=4 )
        {
            return "osoby";
        }
        else
        {
            return "osob";
        }
    }

    private String renderRoomCapacities(DataModel dataModel, FloorVariant floorVariant, String path, Configuration configuration, String selectionType)
    {
        StringBuffer out = new StringBuffer();
        int index = 0;
        for (Integer roomCapacity: floorVariant.getRoomCapacities())
        {
            String color = getColor(dataModel, index);
            out.append(item(path, color, "capacity_" + index, String.format("%d %s", roomCapacity, renderCapacityForOneRoom(roomCapacity)), configuration, selectionType));
            index++;
        }
        return out.toString();
    }

    private String renderRoomAvailabilities(DataModel dataModel, FloorVariant floorVariant, String path, Configuration configuration, String selectionType)
    {
        StringBuffer out = new StringBuffer();
        int index = 0;
        for (Integer roomAvailability : floorVariant.getRoomAvailabilities())
        {
            String color = getColor(dataModel, index);
            String listItem = "obsazeno";
            if (roomAvailability > 0)
            {
                listItem = String.format("%d %s", roomAvailability, renderCapacityForOneRoom(roomAvailability));
            }
            out.append(item(path, color, "availability_" + index, listItem, configuration, selectionType));
            index++;
        }
        return out.toString();
    }

    private String renderCharterer(DataModel dataModel, FloorVariant floorVariant, String path, Configuration configuration, String selectionType)
    {
        StringBuffer out = new StringBuffer();
        boolean moreThanOneCharterer = false;
        boolean noCharterers = false;
        boolean common = false;
        for (Room room : floorVariant.getRooms().values())
        {
            if (room.hasMoreCharterers())
            {
                moreThanOneCharterer = true;
                break;
            }
            if (!room.hasChartererSet() || room.getCharterers().isEmpty())
            {
                if (!"Ostatni".equals(room.getName()))
                {
                    noCharterers = true;
                }
            }
        }
        int index = 3;
        for (String roomCharterer : floorVariant.getRoomCharterers())
        {
            if (!"C Společné".equals(roomCharterer))
            {
                String color = getColor(dataModel, index);
                out.append(item(path, color, "owner_" + index, roomCharterer.substring(2), configuration, selectionType));
                index++;
            }
            else
            {
                common = true;
            }
        }
        if (common)
        {
            String color = getColor(dataModel, 1);
            out.append(item(path, color, "owner_common", "Společné", configuration, selectionType));
            index++;
        }
        if (noCharterers)
        {
            String color = getColor(dataModel, 0);
            out.append(item(path, color, "owner_free", "Volné", configuration, selectionType));
            index++;
        }
        if (moreThanOneCharterer)
        {
            String color = getColor(dataModel, 2);
            out.append(item(path, color, "owner_MoreThanOne", "Více nájemců", configuration, selectionType));
        }
        return out.toString();
    }

    private String renderRoomOccupation(DataModel dataModel, FloorVariant floorVariant, String path, Configuration configuration, String selectionType)
    {
        StringBuffer out = new StringBuffer();
        boolean showInternal = false;
        boolean showExternal = false;
        //boolean showIntExt = false;
        boolean showFlat = false;
        boolean showCommon = false;
        boolean showFree = false;
        for (Room room : floorVariant.getRooms().values())
        {
            boolean roomHasInternal = false;
            boolean roomHasExternal = false;
            boolean roomHasCommon = false;
            boolean roomHasFlat = false;
            boolean roomIsFree = false;
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
            if (room.isFreeSet() && room.getFree() == 1)
            {
                roomIsFree = true;
            }
            showInternal |= roomHasInternal;
            showExternal |= roomHasExternal;
            showCommon |= roomHasCommon;
            showFlat |= roomHasFlat;
            showFree |= roomIsFree;
        }
        if (showInternal)
        {
            String color = getColor(dataModel, 2);
            out.append(item(path, color, "occupation_internal", "Interní", configuration, selectionType));
        }
        if (showExternal)
        {
            String color = getColor(dataModel, 3);
            out.append(item(path, color, "occupation_external", "Externí", configuration, selectionType));
        }
        if (showFlat)
        {
            String color = getColor(dataModel, 4);
            out.append(item(path, color, "occupation_flat", "Byt", configuration, selectionType));
        }
        if (showCommon)
        {
            String color = getColor(dataModel, 1);
            out.append(item(path, color, "occupation_common", "Společné", configuration, selectionType));
        }
        if (showFree)
        {
            String color = getColor(dataModel, 0);
            out.append(item(path, color, "occupation_free", "Volné", configuration, selectionType));
        }
        return out.toString();
    }

    /*
    private void renderBuildings(DataModel dataModel, Configuration configuration, StringBuffer out)
    {
        out.append("<ul class='mktree'><span style='color:#600'>Budovy</span>\n");
        for (Map.Entry<String, Building> buildingPair : dataModel.getBuildings().entrySet())
        {
            String sapId = buildingPair.getKey();
            Building building = buildingPair.getValue();
            out.append("    <li class='liOpen'>" + building.getName());
            renderFloors(building, configuration, out, sapId);
            out.append("    </li>\n");
        }
        out.append("</ul>\n");
    }

    private void renderFloors(Building building, Configuration configuration, StringBuffer out, String buildingSapId)
    {
        out.append("    <ul><span style='color:#600'>Podlaží</span>\n");
        for (Map.Entry<String, Floor> floorPair : building.getFloors().entrySet())
        {
            String sapId = floorPair.getKey();
            Floor floor = floorPair.getValue();
            out.append("        <li class='liOpen'>"+floor.getName());
            renderFloorVariants(floor, configuration, out, buildingSapId, sapId);
            out.append("        </li>\n");
        }
        out.append("    </ul>\n");
    }

    private void renderFloorVariants(Floor floor, Configuration configuration, StringBuffer out, String buildingSapId, String floorSapId)
    {
        out.append("        <ul><span style='color:#600'>Varianta</span>\n");
        List<String> keys = new ArrayList<String>(floor.getFloorVariants().keySet());
        Collections.sort(keys);
        String rnd = Long.toString(Double.doubleToLongBits(Math.random()));
        for (String key : keys)
        {
            FloorVariant floorVariant = floor.getFloorVariants().get(key);
            String name = key;
            out.append("            <li><a target='image' href='renderer?building="+buildingSapId+"&floor="+floorSapId+"&floorVariant="+name+"&room=&rnd="+rnd+"' onclick='setTimeout(\"parent.selected.location.reload()\", 500)'>" + name + " - " + floorVariant.getDate() + "</a>");
            //renderRooms(floorVariant, configuration, out, buildingSapId, floorSapId, key);
            out.append("            </li>\n");
        }
        out.append("        </ul>\n");
    }

    private void renderRooms(FloorVariant floorVariant, Configuration configuration, StringBuffer out, String buildingSapId, String floorSapId, String floorVariantSapId)
    {
        out.append("        <ul><span style='color:#600'>Místnosti</span>\n");
        List<String> keys = new ArrayList<String>(floorVariant.getRooms().keySet());
        Collections.sort(keys);
        String rnd = Long.toString(Double.doubleToLongBits(Math.random()));
        for (String sapId : keys)
        {
            Room room = floorVariant.getRooms().get(sapId);
            //boolean selected = true;
            //selected &= buildingSapId.equals(configuration.building);
            //selected &= floorSapId.equals(configuration.floor);
            //selected &= floorVariantSapId.equals(configuration.floorVariant);
            //selected &= sapId.equals(configuration.room);
            out.append("            <li>" + room.getName()+"\n");
            out.append("                <ul>\n");
            out.append("                    <li><a target='image' href='renderer?building="+buildingSapId+"&floor="+floorSapId+"&floorVariant="+floorVariantSapId+"&room="+sapId+"&rnd="+rnd+"' onclick='setTimeout(\"parent.selected.location.reload()\", 500)'>"+sapId+"</a></li>\n");
            out.append("                    <li>Plocha: " + room.getArea()+"</li>\n");
            out.append("                </ul>\n");
            out.append("            </li>\n");
        }
        out.append("        </ul>\n");
    }
    */

}

