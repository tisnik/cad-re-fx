package cz.tisnik.cadgfx.servlets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cz.tisnik.cadgfx.MapRegion;
import cz.tisnik.cadgfx.MapRegions;

public class MapRenderer extends CustomHttpServlet
{
    /**
     * Generated serial version ID
     */
    private static final long serialVersionUID = -3779368499457184387L;

    private static final Color ColorRed = Color.RED;
    private static final Color ColorBlue = Color.BLUE;
    private static final Color ColorGreen = Color.GREEN;

    @Override
    public void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException
    {
        this.log.logBegin("doProcess");
        HttpSession session = request.getSession();
        String map = request.getParameter("map") != null ? request.getParameter("map") : (String)session.getAttribute("map");
        String region = request.getParameter("region") != null ? request.getParameter("region") : (String)session.getAttribute("region");
        Integer x = null;
        Integer y = null;
        String rnd = request.getParameter("rnd");
        try
        {
            x = Integer.parseInt(request.getParameter("coordsx"));
            y = Integer.parseInt(request.getParameter("coordsy"));
        }
        catch (NumberFormatException e)
        {
            // it's correct, when coordinates are not set
        }
        this.log.logSet("coordsx", x);
        this.log.logSet("coordsy", y);
        this.log.logSet("map", map);
        this.log.logSet("region", region);
        this.log.logSet("rnd", rnd);
        if (map != null && x != null && y != null)
        {
            session.setAttribute("map", map);
            for (MapRegion reg : MapRegions.getMapRegions(map))
            {
                if (reg.getPolygon().contains(x, y))
                {
                    region = reg.getId();
                    this.log.log("found region " + region);
                }
            }
        }
        if (region != null)
        {
            session.setAttribute("region", map);
        }
        renderImage(this.getServletContext(), request, response, map, region);
        this.log.logEnd("doProcess");
    }

    private void writeImage(HttpServletResponse response, BufferedImage bi) throws IOException
    {
        response.setHeader( "Pragma", "no-cache" );
        response.addHeader( "Cache-Control", "must-revalidate" );
        response.addHeader( "Cache-Control", "no-cache" );
        response.addHeader( "Cache-Control", "no-store" );
        response.setDateHeader("Expires", 0);
        response.setContentType("image/png;charset=utf-8");
        OutputStream o = response.getOutputStream();
        try
        {
            ImageIO.write(bi, "png", o);
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
        o.flush();
        o.close();
    }

    private static BufferedImage readImage(String directory, String fileName) throws IOException
    {
        File imageFile = new File(directory, fileName);
        return ImageIO.read(imageFile);
    }

    private void renderImage(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response, String map, String region)
            throws IOException
    {
        this.log.logBegin("renderImage()");
        
        BufferedImage bi = readImage(servletContext.getRealPath("maps"), map+".jpg");
        Graphics2D gc = bi.createGraphics();

        Font font = new Font("Helvetica", Font.PLAIN, 10);
        gc.setFont(font);
        gc.setStroke(new BasicStroke(3));

        drawPolygons(gc, map, region);

        writeImage(response, bi);
        this.log.log("image written");
        this.log.logEnd("renderImage()");
    }

    private void drawPolygons(Graphics2D gc, String mapName, String regionName)
    {
        for (MapRegion region : MapRegions.getMapRegions(mapName))
        {
            boolean building = region.isBuilding();
            Color color = building ? ColorBlue : ColorRed;
            gc.setColor(color);
            gc.drawPolygon(region.getPolygon());
        }
        if (regionName != null)
        {
            for (MapRegion region : MapRegions.getMapRegions(mapName))
            {
                if (regionName.equals(region.getId()))
                {
                    Polygon p = region.getPolygon();
                    gc.setColor(new Color(1.0f, 1.0f, 0.0f, 0.5f));
                    gc.fillPolygon(p);
                    gc.setColor(ColorGreen);
                    gc.drawPolygon(p);
                }
            }
        }
    }

}
