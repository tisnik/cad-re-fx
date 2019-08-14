package cz.tisnik.cadgfx.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import cz.tisnik.cadgfx.utils.Log;

public class Pozadavky
{
    /**
     * Instance objektu použitého pro logování do logovacího souboru či na
     * standardní výstup servlet kontejneru.
     */
    private Log log = new Log( this.getClass().getName() );

    private static Map<String, String> pozadavky = new HashMap<String, String>();

    public Pozadavky(ServletContext servletContext)
    {
        this.log.logBegin("readPozadavky");
        BufferedReader in = null;
        try
        {
            this.log.logBegin("opening input data stream");
            in = new BufferedReader(new InputStreamReader(servletContext.getResourceAsStream("/data/pozadavky.txt"), "UTF-8"));
            String line;
            while ((line=in.readLine()) != null)
            {
                line = line.trim();
                if (!line.isEmpty())
                {
                    String[] record = line.split(",");
                    String key = record[0];
                    String value = record[1];
                    pozadavky.put(key, value);
                    this.log.log(String.format("pozadavek %s = %s", key, value));
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
        this.log.logBegin("readPozadavky");
    }

    public String getPozadavek(String key)
    {
        return pozadavky.get(key);
    }
}
