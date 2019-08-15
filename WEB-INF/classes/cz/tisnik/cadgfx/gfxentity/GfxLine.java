package cz.tisnik.cadgfx.gfxentity;

import java.awt.Graphics2D;

import cz.tisnik.cadgfx.utils.MathCommon;

public class GfxLine implements GfxEntity
{
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private GfxEntityAttribute attribute;

    public GfxLine()
    {
        this(0, 0, 0, 0, GfxEntityAttribute.UNKNOWN);
    }

    public GfxLine(int x1, int y1, int x2, int y2, GfxEntityAttribute attribute)
    {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.attribute = attribute;
    }

    public GfxLine(String value)
    {
        String[] values = value.trim().split("\\s+");
        try
        {
            this.x1 = MathCommon.doubleStr2int(values[0]);
            this.y1 = MathCommon.doubleStr2int(values[1]);
            this.x2 = MathCommon.doubleStr2int(values[2]);
            this.y2 = MathCommon.doubleStr2int(values[3]);
            if (values.length > 4)
            {
                this.attribute = GfxEntityAttribute.resolveGfxEntityAttribute(values[4]);
            }
        }
        catch (Exception e)
        {
            System.out.println("***" + value + "***");
            e.printStackTrace();
        }
    }

    public int getX1()
    {
        return x1;
    }

    public int getY1()
    {
        return y1;
    }

    public int getX2()
    {
        return x2;
    }

    public int getY2()
    {
        return y2;
    }

    public GfxEntityAttribute getAttribute()
    {
        return attribute;
    }

    public String toString()
    {
        return String.format("line %3d %3d %3d %3d", this.getX1(), this.getY1(), this.getX2(), this.getY2());
    }

    public void draw(Graphics2D gc)
    {
        gc.drawLine(this.getX1(), this.getY1(), this.getX2(), this.getY2());
    }
}
