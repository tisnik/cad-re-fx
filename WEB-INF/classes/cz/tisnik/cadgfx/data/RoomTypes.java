package cz.tisnik.cadgfx.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import javax.servlet.ServletContext;

import cz.tisnik.cadgfx.utils.Log;

public class RoomTypes
{
    /**
     * Instance objektu použitého pro logování do logovacího souboru či na
     * standardní výstup servlet kontejneru.
     */
    private Log log = new Log( this.getClass().getName() );

    private static final Map<Integer, String> types = new HashMap<Integer, String>();

    public RoomTypes(ServletContext servletContext)
    {
        this.log.logBegin("readRoomTypes");
        BufferedReader in = null;
        try
        {
            this.log.logBegin("opening input data stream");
            in = new BufferedReader(new InputStreamReader(servletContext.getResourceAsStream("/data/room_types.txt"), "UTF-8"));
            String line;
            while ((line=in.readLine()) != null)
            {
                line = line.trim();
                if (!line.isEmpty())
                {
                    String[] record = line.split(",");
                    types.put(Integer.parseInt(record[0]), record[1]);
                    this.log.log("record: " + record[0] + " -> " + record[1]);
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
        this.log.logEnd("readRoomTypes");
    }

    public String getRoomType(Integer key)
    {
        return types.get(key);
    }
}
