package cz.tisnik.cadgfx.servlets;

import java.awt.Polygon;
import java.io.IOException;

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
import cz.tisnik.cadgfx.gfxentity.GfxArc;
import cz.tisnik.cadgfx.gfxentity.GfxCircle;
import cz.tisnik.cadgfx.gfxentity.GfxEntity;
import cz.tisnik.cadgfx.gfxentity.GfxLine;
import cz.tisnik.cadgfx.gfxentity.GfxText;
import cz.tisnik.cadgfx.utils.Log;

public class Info extends HttpServlet
{
    /**
     * Generated serial version ID.
     */

    private static final long serialVersionUID = 3581813001836771104L;
    /**
     * Instance objektu použitého pro logování do logovacího souboru či na
     * standardní výstup servlet kontejneru.
     */
    private Log log = new Log( this.getClass().getName() );

    public void init()
    {
        this.log.log("Info.init()");
    }

    public void destroy()
    {
        this.log.log("Info.destroy()");
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

    public void doProcess(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        this.log.logBegin("doProcess()");
        ServletContext servletContext = this.getServletContext();
        HttpSession session = request.getSession();
        Configuration configuration = (Configuration) session.getAttribute( "configuration" );
        DataModel dataModel = (DataModel) servletContext.getAttribute("data");
        if (configuration == null)
        {
            configuration = new Configuration(servletContext, dataModel);
            System.out.println("Index constructed configuration");
        }

        StringBuffer out = new StringBuffer("");
        renderInfo(dataModel, configuration, out);
        request.setAttribute("info", out.toString());

        servletContext.getRequestDispatcher( "/jsp/info.jsp" ).forward( request, response );
        this.log.logEnd("doProcess()");
    }

    private void renderInfo(DataModel dataModel, Configuration configuration, StringBuffer out)
    {
        Building building = dataModel.getBuilding(configuration.building);
        Floor floor = building == null ? null : building.getFloors().get(configuration.floor);
        FloorVariant floorVariant = floor == null ? null :floor.getFloorVariants().get(configuration.floorVariant);
        Room room = floorVariant == null ? null : floorVariant.getRooms().get(configuration.room);

        out.append("<table border='2' frame='border' rules='rows' cellspacing='1' cellpadding='1' class='formular' summary=''>\n");

        out.append("<tr><td style='color:#008' colspan='2'>Výběr</td></tr>\n");
        out.append("<tr><td style='color:#800'>Budova: </td>");
        if (building != null)
        {
            out.append("<td style='color:#000'>"+building.getName()+"</td>\n");
        }
        else
        {
            out.append("<td style='color:#f00'>nenalezeno</td>\n");
        }
        out.append("</tr>\n");

        out.append("<tr><td style='color:#800'>Podlaží: </td>");
        if (floor != null)
        {
            out.append("<td style='color:#000'>"+floor.getName()+"</td>\n");
        }
        else
        {
            out.append("<td style='color:#f00'>nenalezeno</td>\n");
        }
        out.append("</tr>\n");

        out.append("<tr><td style='color:#800'>Varianta: </td>");
        if (floorVariant != null)
        {
            out.append("<td style='color:#000'>"+configuration.floorVariant+" ("+floorVariant.getDate()+")</td>\n");
        }
        else
        {
            out.append("<td style='color:#f00'>nenalezeno</td>\n");
        }
        out.append("</tr>\n");

        out.append("<tr><td style='color:#800'>Místnost: </td>");
        if (room != null)
        {
            out.append("<td style='color:#000'>"+configuration.room+"</td>\n");
        }
        else
        {
            out.append("<td style='color:#f00'>nenalezeno</td>\n");
        }
        out.append("</tr>\n");

        int buildings = 0;
        int floors = 0;
        int variants = 0;
        int rooms = 0;
        int lines = 0;
        int circles = 0;
        int arcs = 0;
        int polygons = 0;
        int points = 0;
        int texts = 0;

        for (Building selectedBuilding : dataModel.getBuildings().values())
        {
            buildings++;
            for (Floor selectedFloor : selectedBuilding.getFloors().values())
            {
                floors++;
                for (FloorVariant selectedVariant : selectedFloor.getFloorVariants().values())
                {
                    variants++;
                    for (Room selectedRoom : selectedVariant.getRooms().values())
                    {
                        rooms++;
                        for (GfxEntity entity : selectedRoom.getEntities())
                        {
                            if (entity instanceof GfxLine)
                            {
                                lines++;
                            }
                            if (entity instanceof GfxCircle)
                            {
                                circles++;
                            }
                            if (entity instanceof GfxArc)
                            {
                                arcs++;
                            }
                            if (entity instanceof GfxText)
                            {
                                texts++;
                            }
                        }
                        polygons += selectedRoom.getPolygons().size();
                        for (Polygon polygon : selectedRoom.getPolygons())
                        {
                            points += polygon.npoints;
                        }
                    }
                }
            }
        }

        out.append("<tr><td colspan='2'>&nbsp;</td></tr>\n");
        out.append("<tr><td style='color:#008' colspan='2'>Datový model</td></tr>\n");

        out.append("<tr><td style='color:#800'>Budov:</td>");
        out.append("<td>" + buildings + "</td></tr>");

        out.append("<tr><td style='color:#800'>Pater:</td>");
        out.append("<td>" + floors + "</td></tr>");

        out.append("<tr><td style='color:#800'>Variant:</td>");
        out.append("<td>" + variants + "</td></tr>");

        out.append("<tr><td style='color:#800'>Místností:</td>");
        out.append("<td>" + rooms + "</td></tr>");

        out.append("<tr><td style='color:#800'>Úseček:</td>");
        out.append("<td>" + lines + "</td></tr>");

        out.append("<tr><td style='color:#800'>Kružnic:</td>");
        out.append("<td>" + circles + "</td></tr>");

        out.append("<tr><td style='color:#800'>Oblouků:</td>");
        out.append("<td>" + arcs + "</td></tr>");

        out.append("<tr><td style='color:#800'>Polygonů:</td>");
        out.append("<td>" + polygons + "</td></tr>");

        out.append("<tr><td style='color:#800'>Vrcholů polygonů:</td>");
        out.append("<td>" + points + "</td></tr>");

        out.append("<tr><td style='color:#800'>Vrcholů/polygon:</td>");
        out.append("<td>" + ((float)points/polygons) + "</td></tr>");

        out.append("<tr><td style='color:#800'>Textů:</td>");
        out.append("<td>" + texts + "</td></tr>");

        out.append("</table>\n");
    }

}
