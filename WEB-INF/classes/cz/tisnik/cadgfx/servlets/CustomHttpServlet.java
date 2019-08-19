package cz.tisnik.cadgfx.servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cz.tisnik.cadgfx.Controller;
import cz.tisnik.cadgfx.utils.Log;

public abstract class CustomHttpServlet extends HttpServlet
{
    /**
     * Generated serial version ID.
     */
    private static final long serialVersionUID = -209200846352955173L;

    /**
     * Instance of class used for output log information either to the standard
     * output or to log file whose name is specified in web.xml.
     */
    protected Log log = new Log( this.getClass().getName() );

    /**
     * Called by the servlet container to indicate to a servlet that the servlet
     * is being placed into service.
     */
    @Override
    public void init()
    {
        this.log.log("Index.init()");
    }

    /**
     * Called by the servlet container to indicate to a servlet that the servlet
     * is being taken out of service.
     */
    public void destroy()
    {
        this.log.log("Index.destroy()");
    }

    /**
     * Called by the server (via the service method) to allow a servlet to
     * handle a GET request.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        this.log.logBegin("doGet()");
        doProcess(request, response);
        this.log.logEnd("doGet()");
    }

    /**
     * Called by the server (via the service method) to allow a servlet to
     * handle a POST request. The HTTP POST method allows the client to send
     * data of unlimited length to the Web server a single time.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        this.log.logBegin("doPost()");
        doProcess(request, response);
        this.log.logEnd("doPost()");
    }

    /**
     * Receives an HTTP HEAD request from the protected service method and
     * handles the request. The client sends a HEAD request when it wants to see
     * only the headers of a response, such as Content-Type or Content-Length.
     * The HTTP HEAD method counts the output bytes in the response to set the
     * Content-Length header accurately.
     */
    public void doHead(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, java.io.IOException
    {
        this.log.log("doHead()");
    }

    /**
     * Called by the server (via the service method) to allow a servlet to
     * handle a PUT request. The PUT operation allows a client to place a file
     * on the server and is similar to sending a file by FTP.
     */
    public void doPut(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, java.io.IOException
    {
        this.log.log("doPut()");
    }

    /**
     * Called from doGet() and doPost() method.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public abstract void doProcess(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException;

    /**
     * This method redirects application to the servlet or JSP page occording to
     * redirect information specified in Controller class.
     *
     * @param servletContext
     * @param request
     * @param response
     * @param status
     * @throws ServletException
     * @throws IOException
     */
    public void redirectTo(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response, String status)
        throws ServletException, IOException
    {
        servletContext.getRequestDispatcher( Controller.getRedirectUrl(this.getClass(), status) ).forward( request, response );
    }
}
