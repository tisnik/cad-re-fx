package cz.tisnik.cadgfx;

import java.util.HashMap;
import java.util.Map;

import cz.tisnik.cadgfx.utils.Log;

public class Controller
{
    /**
     * Instance of class used for output log information either to the standard
     * output or to log file whose name is specified in web.xml.
     */
    private static final Log log = new Log( Controller.class.getName() );

    private static final char SEPARATION_CHAR = '#';

    /**
     * This map contains redirection URLs. There could exists more redirections
     * for each servlet because class name of the servlet is concatenated with
     * it's status code(s). Servlet name must be separated by "#" from status code.
     */
    private static final Map<String, String> redirectionUrls = new HashMap<String, String>();

    static
    {
        redirectionUrls.put("Maps#ok", "/jsp/Maps.jsp");
        redirectionUrls.put("MapsLeftMenu#ok", "/jsp/MapsLeftMenu.jsp");
    }

    @SuppressWarnings("unchecked")
    public static String getRedirectUrl(Class clazz, String status)
    {
        String className = clazz.getName();
        log.logSet("class name:", className);
        className = clazz.getName().substring(1 + clazz.getName().lastIndexOf('.'));
        log.logSet("simple class name:", className);
        log.logSet("status: ", status);
        String redirectionString = redirectionUrls.get(className + SEPARATION_CHAR + status.toLowerCase());
        if (redirectionString != null)
        {
            log.logSet("redirected to: ", redirectionString);
        }
        else
        {
            log.logError("redirection not found");
        }
        return redirectionString;
    }

}
