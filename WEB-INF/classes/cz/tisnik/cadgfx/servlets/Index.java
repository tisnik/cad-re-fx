package cz.tisnik.cadgfx.servlets;

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
import cz.tisnik.cadgfx.utils.Log;

public class Index extends HttpServlet
{
    /**
     * Generated serial version ID.
     */
    private static final long serialVersionUID = 197073842801485910L;

    /**
     * Instance objektu použitého pro logování do logovacího souboru či na
     * standardní výstup servlet kontejneru.
     */
    private Log log = new Log( this.getClass().getName() );

    public void init()
    {
        this.log.log("Index.init()");
    }

    public void destroy()
    {
        this.log.log("Index.destroy()");
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
        Configuration configuration = (Configuration) session.getAttribute("configuration");
        DataModel dataModel = (DataModel) servletContext.getAttribute("data");
        if (configuration == null)
        {
            configuration = new Configuration(servletContext, dataModel);
            this.log.log("$RED$Index constructed configuration");
            session.setAttribute("configuration", configuration);
        }

        StringBuffer out = new StringBuffer("");
        renderSelectedObject(dataModel, configuration, out);
        request.setAttribute("selectedObject", out.toString());

        servletContext.getRequestDispatcher( "/jsp/index.jsp" ).forward( request, response );
        this.log.logEnd("doProcess()");
    }

    private void renderSelectedObject(DataModel dataModel, Configuration configuration, StringBuffer out)
    {
        Building building = dataModel.getBuilding(configuration.building);
        Floor floor = building == null ? null : building.getFloors().get(configuration.floor);
        FloorVariant floorVariant = floor == null ? null :floor.getFloorVariants().get(configuration.floorVariant);
        //Room room = floorVariant == null ? null : floorVariant.getRooms().get(configuration.room);

        out.append("<span style='color:#800'>Budova: </span>");
        if (building != null)
        {
            out.append("<span style='color:#000'>"+building.getName()+"&nbsp;</span>\n");
        }
        else
        {
            out.append("<span style='color:#f00'>nenalezeno</span>\n");
        }

        out.append("<span style='color:#800'>Podlaží: </span>");
        if (floor != null)
        {
            out.append("<span style='color:#000'>"+floor.getName()+"&nbsp;</span>\n");
        }
        else
        {
            out.append("<span style='color:#f00'>nenalezeno</span>\n");
        }

        out.append("<span style='color:#800'>Varianta: </span>");
        if (floorVariant != null)
        {
            out.append("<span style='color:#000'>"+configuration.floorVariant+" ("+floorVariant.getDate()+")&nbsp;</span>\n");
        }
        else
        {
            out.append("<span style='color:#f00'>nenalezeno</span>\n");
        }
    }

}

