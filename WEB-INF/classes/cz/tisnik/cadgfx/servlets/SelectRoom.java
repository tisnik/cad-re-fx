package cz.tisnik.cadgfx.servlets;

import java.io.IOException;
import java.util.Map;

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
import cz.tisnik.cadgfx.utils.Log;

public class SelectRoom extends HttpServlet
{
    /**
     * Generated serial version ID.
     */
    private static final long serialVersionUID = -8756370137935203157L;

    /**
     * Instance objektu použitého pro logování do logovacího souboru či na
     * standardní výstup servlet kontejneru.
     */
    private Log log = new Log( this.getClass().getName() );

    public void init()
    {
        this.log.log("SelectRoom.init()");
    }

    public void destroy()
    {
        this.log.log("SelectRoom.destroy()");
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
        String floorIdParamName = servletContext.getInitParameter("param_name_from_sap_floor_id");
        String roomIdParamName = servletContext.getInitParameter("param_name_from_sap_room_id");
        String versionParamName = servletContext.getInitParameter("param_name_from_sap_version");

        if (configuration == null)
        {
            configuration = new Configuration(this.getServletContext(), dataModel);
            this.log.log("$RED$constructed configuration");
        }
        this.log.logSet("building", ""+configuration.building);

        if (request.getParameter(versionParamName)!=null)
        {
            configuration.floorVariant = request.getParameter(versionParamName);
            this.log.logSet("variant", configuration.floorVariant);
        }
        if (request.getParameter(floorIdParamName)!=null)
        {
            configuration.floor = request.getParameter(floorIdParamName);
            this.log.logSet("floor", configuration.floor);
        }
        for (Map.Entry<String, Building> building : dataModel.getBuildings().entrySet())
        {
            if (building.getValue().getFloors().containsKey(configuration.floor))
            {
                this.log.log("floor " + configuration.floor + " has been found in building " + building.getKey());
                configuration.building = building.getKey();
            }
        }
        if (request.getParameter(roomIdParamName)!=null)
        {
            configuration.room = request.getParameter(roomIdParamName);
            this.log.logSet("room", configuration.room);
        }
        this.log.log("building exist", Boolean.toString(dataModel.getBuilding(configuration.building) != null));
        this.log.logSet("building", configuration.building);
        this.log.logSet("floor", configuration.floor);
        Floor floor = dataModel.getBuilding(configuration.building).getFloor(configuration.floor);
        this.log.log("floor exist", Boolean.toString(floor != null));
        this.log.log("variant exist", Boolean.toString(floor.getFloorVariant(configuration.floorVariant) != null));

        configuration.defaultXpos = configuration.xpos = floor.getDefaultXpos();
        configuration.defaultYpos = configuration.ypos = floor.getDefaultYpos();
        configuration.defaultScale = configuration.scale = floor.getDefaultScale();

        configuration.clearDisabledColors();
        session.setAttribute("configuration", configuration);

        servletContext.getRequestDispatcher( "/Index" ).forward( request, response );
        this.log.logEnd("doProcess()");
    }
}

