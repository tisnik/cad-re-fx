package cz.tisnik.cadgfx;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import cz.tisnik.cadgfx.data.DataModel;
import cz.tisnik.cadgfx.data.Floor;
import cz.tisnik.cadgfx.utils.Log;

public class Configuration
{
    /**
     * Instance objektu použitého pro logování do logovacího souboru či na
     * standardní výstup servlet kontejneru.
     */
    private Log log = new Log( this.getClass().getName() );

    public int imageWidth;
    public int imageHeight;
    public boolean debugSapEvent;
    public boolean debugSelectedFloor;
    public double defaultXpos = 0;//150.0;
    public double defaultYpos = 0;//120.0;
    public double defaultScale = 1;//1.5;
    public double xpos = defaultXpos;
    public double ypos = defaultYpos;
    public double scale = defaultScale;
    public String building;
    public String floor;
    public String floorVariant;
    public String room;
    public String selectType = null;
    public Integer coordsx = null;
    public Integer coordsy = null;
    public Map<String, Set<String>> disabledColors = null;

    public Configuration(ServletContext servletContext, DataModel dataModel)
    {
        this.log.logBegin("constructor");
        this.disabledColors = new HashMap<String, Set<String>>();
        this.building = servletContext.getInitParameter("default_building");
        this.floor = servletContext.getInitParameter("default_floor");
        this.floorVariant = servletContext.getInitParameter("default_floor_variant");
        this.room = "";
        this.debugSapEvent = "true".equals(servletContext.getInitParameter("debug_sapevent"));
        this.debugSelectedFloor = "true".equals(servletContext.getInitParameter("debug_selected_floor"));
        try
        {
            this.log.logSet("building", this.building);
            this.log.logSet("floor", this.floor);
            try
            {
                Floor floor = dataModel.getBuilding(this.building).getFloor(this.floor);
                this.defaultXpos = this.xpos = floor.getDefaultXpos();
                this.defaultYpos = this.ypos = floor.getDefaultYpos();
                this.defaultScale = this.scale = floor.getDefaultScale();
            }
            catch (Exception e)
            {
                this.log.logError("cannot set default xpos, ypos and scale");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            this.imageWidth = Integer.parseInt(servletContext.getInitParameter("image_width"));
            this.imageHeight = Integer.parseInt(servletContext.getInitParameter("image_height"));
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
            this.imageWidth = 640;
            this.imageHeight = 480;
        }
        this.log.logEnd("constructor");
    }

    public void clearDisabledColors()
    {
        this.disabledColors = new HashMap<String, Set<String>>();
    }
}
