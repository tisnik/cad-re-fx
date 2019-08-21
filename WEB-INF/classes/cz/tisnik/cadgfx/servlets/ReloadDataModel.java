package cz.tisnik.cadgfx.servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cz.tisnik.cadgfx.data.DataModel;
import cz.tisnik.cadgfx.data.DataModelInFiles;
import cz.tisnik.cadgfx.utils.Log;
import cz.tisnik.cadgfx.utils.WebAppCommon;

public class ReloadDataModel extends HttpServlet
{

    /**
     * Generated serial version ID.
     */
    private static final long serialVersionUID = -3095530987224866671L;

    /**
     * Instance objektu použitého pro logování do logovacího souboru či na
     * standardní výstup servlet kontejneru.
     */
    private Log log = new Log( this.getClass().getName() );

    public void init()
    {
        this.log.log("ReloadDataModel.init()");
    }

    public void destroy()
    {
        this.log.log("ReloadDataModel.destroy()");
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
        boolean debugInitDataModel = WebAppCommon.isDebugInitDataModel(servletContext);
        boolean debugFetchValueObject = WebAppCommon.isDebugFetchValueObject(servletContext);
        DataModelInFiles dmif = new DataModelInFiles();
        DataModel dataModel = dmif.readDataModel( servletContext, debugInitDataModel, debugFetchValueObject );
        dmif.dumpDataModel( servletContext, dataModel);
        servletContext.removeAttribute( "data" );
        servletContext.removeAttribute( "initialized" );
        servletContext.setAttribute( "data", dataModel );
        servletContext.setAttribute( "initialized", true );

        servletContext.getRequestDispatcher( "/admin.html" ).forward( request, response );
        this.log.logEnd("doProcess()");
    }

}
