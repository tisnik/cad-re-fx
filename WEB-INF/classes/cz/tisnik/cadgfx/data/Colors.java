package cz.tisnik.cadgfx.data;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import cz.tisnik.cadgfx.utils.Log;

public class Colors
{
    /**
     * Instance objektu použitého pro logování do logovacího souboru či na
     * standardní výstup servlet kontejneru.
     */
    private Log log = new Log( this.getClass().getName() );

    private static List<Color> colors = new ArrayList<Color>();

    public Colors(ServletContext servletContext)
    {
        this.log.logBegin("readColors");
        BufferedReader in = null;
        try
        {
            this.log.logBegin("opening input data stream");
            in = new BufferedReader(new InputStreamReader(servletContext.getResourceAsStream("/data/colors.txt"), "UTF-8"));
            String line;
            while ((line=in.readLine()) != null)
            {
                line = line.trim();
                if (!line.isEmpty())
                {
                    int r = Integer.parseInt(line.substring(0,2), 16);
                    int g = Integer.parseInt(line.substring(2,4), 16);
                    int b = Integer.parseInt(line.substring(4,6), 16);
                    colors.add(new Color(r, g, b));
                    this.log.log(String.format("color %d = %02x %02x %02x", colors.size(), r, g, b));
                }
            }
        }
        catch (IOException e)
        {
            this.log.log("$RED$read error $GRAY$: " + e.getMessage());
            e.printStackTrace();
        }
        try
        {
            if (in != null)
            {
                this.log.logEnd("closing input data stream");
                in.close();
            }
        }
        catch (IOException e)
        {
            this.log.log("$RED$stream close error $GRAY$: " + e.getMessage());
            e.printStackTrace();
        }
        this.log.logEnd("readColors");
    }

    public Color getColor(int index)
    {
        return colors.get(index % colors.size());
    }

}
