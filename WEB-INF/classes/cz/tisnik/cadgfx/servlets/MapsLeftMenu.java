package cz.tisnik.cadgfx.servlets;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cz.tisnik.cadgfx.MapRegion;
import cz.tisnik.cadgfx.MapRegions;

public class MapsLeftMenu extends CustomHttpServlet
{
    /**
     * Generated serial version ID
     */
    private static final long serialVersionUID = -4965754797351745015L;

    @Override
    public void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException
    {
        this.log.logBegin("doProcess()");
        ServletContext servletContext = this.getServletContext();

        String selectedMap = request.getParameter("map");
        String selectedRegion = request.getParameter("region");
        this.log.logSet("map", selectedMap);
        this.log.logSet("region", selectedRegion);
        request.setAttribute("mapList", prepareMapList(servletContext.getRealPath("maps/")));
        request.setAttribute("objList", prepareObjectList(selectedMap, selectedRegion));

        this.redirectTo(servletContext, request, response, "ok");
        this.log.logEnd("doProcess()");
    }

    private String prepareMapList(String directory)
    {
        StringBuffer out = new StringBuffer();
        File files = new File(directory);
        for (String file : files.list())
        {
            String name = file.substring(0, file.indexOf('.'));
            out.append(String.format("<tr><td><a href='MapsLeftMenu?map=%s' onClick='onMapClick(\"%s\")'>%s</a></td></tr>\n", name, name, name));
        }
        return out.toString();
    }

    private String prepareObjectList(String selectedMap, String selectedRegion)
    {
        if (selectedMap == null)
        {
            return "";
        }
        StringBuffer out = new StringBuffer();
        List<MapRegion> mapRegions = MapRegions.getMapRegions(selectedMap);
        if (mapRegions == null)
        {
            return "";
        }
        for (MapRegion mapRegion : mapRegions)
        {
            String id = mapRegion.getId();
            String name = mapRegion.getName();
            String type = mapRegion.isBuilding() ? "budova" : "pozemek";
            boolean selected = mapRegion.equals(selectedRegion);
            String style = selected ? " style='background-color:#ccaaaa" : "";
            out.append(String.format("<tr><td %s><a href='MapsLeftMenu?map=%s&region=%s' onclick='onRegionClick(\"%s\",\"%s\")'>%s</a></td><td>%s</td></tr>", style, selectedMap, id, selectedMap, id, name, type));
        }
        return out.toString();
    }

}
