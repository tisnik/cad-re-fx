package cz.tisnik.cadgfx.servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cz.tisnik.cadgfx.Configuration;
import cz.tisnik.cadgfx.data.DataModel;
import cz.tisnik.cadgfx.utils.Log;

public class Renderer extends HttpServlet
{
    /**
     * Generated serial version ID.
     */
    private static final long serialVersionUID = -4681215837486378743L;

    private static final double STEP = 16.0;

    /**
     * Instance objektu použitého pro logování do logovacího souboru či na
     * standardní výstup servlet kontejneru.
     */
    private Log log = new Log( this.getClass().getName() );

    public void init()
    {
        this.log.log("$GREEN$renderer.init()");
    }

    public void destroy()
    {
        this.log.log("$MAGENTA$renderer.destroy()");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        this.log.logBegin("doGet()");
        processParameters(request, response);
        //renderImage(request, response);
        this.log.logEnd("doGet()");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        this.log.logBegin("doPost()");
        processParameters(request, response);
        //renderImage(request, response);
        this.log.logEnd("doPost()");
    }

    private void processParameters(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        this.log.logBegin("processParameters()");
        HttpSession session = request.getSession();
        ServletContext servletContext = this.getServletContext();
        Configuration configuration = (Configuration) session.getAttribute( "configuration" );
        DataModel dataModel = (DataModel) servletContext.getAttribute("data");
        if (configuration == null)
        {
            configuration = new Configuration(servletContext, dataModel);
            this.log.log("$RED$renderer constructed configuration");
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
            configuration.scale = 1.0;
        }
        if ("zoom_all".equals(command))
        {
            configuration.scale = 1.0;
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
            this.log.log("set selectType to $BLUE$"+configuration.selectType);
        }

        session.setAttribute( "configuration", configuration );
        servletContext.getRequestDispatcher( "/jsp/renderer.jsp" ).forward( request, response );
        this.log.logEnd("processParameters()");
    }

}

