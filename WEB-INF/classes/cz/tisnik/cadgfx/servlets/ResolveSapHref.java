package cz.tisnik.cadgfx.servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cz.tisnik.cadgfx.Configuration;
import cz.tisnik.cadgfx.utils.Log;

public class ResolveSapHref extends HttpServlet
{
    /**
     * Generated serial version ID.
     */
    private static final long serialVersionUID = -2653096427049087415L;

    /**
     * Instance objektu použitého pro logování do logovacího souboru či na
     * standardní výstup servlet kontejneru.
     */
    private Log log = new Log( this.getClass().getName() );

    public void init()
    {
        this.log.log("ResolveSapHref.init()");
    }

    public void destroy()
    {
        this.log.log("ResolveSapHref.destroy()");
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
        ServletOutputStream out = response.getOutputStream();
        String roomIdparameterName = servletContext.getInitParameter("param_name_to_sap_room_id");
        String random = Long.toString(Double.doubleToLongBits(Math.random()));
        out.print("<a id='sap_href' name='sap_href' href='SAPEVENT:ROOM_CLICK?"+roomIdparameterName+"=" + configuration.room + "&RANDOM="+random+"'>SAP</a>");
        this.log.logEnd("doProcess()");
    }

}

