package cz.tisnik.cadgfx.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cz.tisnik.cadgfx.utils.Log;

public class Help extends HttpServlet
{
    /**
     * Generated serial version ID.
     */
    private static final long serialVersionUID = 7045262816142637440L;

    private final String className = this.getClass().getName();

    /**
     * Instance objektu použitého pro logování do logovacího souboru či na
     * standardní výstup servlet kontejneru.
     */
    private Log log = new Log(className);

    public void init()
    {
        this.log.log(className + " init()");
    }

    public void destroy()
    {
        this.log.log(className + " destroy()");
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
        this.getServletContext().getRequestDispatcher( "/jsp/help.jsp" ).forward( request, response );
        this.log.logEnd("doProcess()");
    }

}

