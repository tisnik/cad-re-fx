package cz.tisnik.cadgfx.utils;

import javax.servlet.ServletContext;

public class WebAppCommon
{
    /**
     * V závislosti na hodnotě inicializačního parametru debug_init_data_model
     * tato metoda vrací pravdivostní hodnotu true nebo false.
     * @param servletContext 
     */
    public static boolean isDebugInitDataModel(ServletContext servletContext)
    {
        return "true".equals(servletContext.getInitParameter("debug_init_data_model"));
    }

    /**
     * V závislosti na hodnotě inicializačního parametru debug_fetch_value_object
     * tato metoda vrací pravdivostní hodnotu true nebo false.
     * @param servletContext 
     */
    public static boolean isDebugFetchValueObject(ServletContext servletContext)
    {
        return "true".equals(servletContext.getInitParameter("debug_fetch_value_object"));
    }

    public static boolean areAdminCommandsEnabled(ServletContext servletContext)
    {
        return "true".equals(servletContext.getInitParameter("enable_admin_commands"));
    }

    public static boolean isDisplayFloorAttributesEnabled(ServletContext servletContext)
    {
        return "true".equals(servletContext.getInitParameter("show_attributes"));
    }

}
