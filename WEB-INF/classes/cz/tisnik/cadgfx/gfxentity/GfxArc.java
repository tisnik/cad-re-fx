package cz.tisnik.cadgfx.gfxentity;

import java.awt.Graphics2D;

import cz.tisnik.cadgfx.utils.MathCommon;

public class GfxArc implements GfxEntity
{
    private int x;
    private int y;
    private int radius;
    private int startAngle;
    private int endAngle;
    private GfxEntityAttribute attribute;

    public GfxArc()
    {
        this(0, 0, 0, 0, 0, GfxEntityAttribute.UNKNOWN);
    }

    public GfxArc(int x, int y, int radius, int startAngle, int endAngle, GfxEntityAttribute attribute)
    {
        super();
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.startAngle = startAngle; 
        this.endAngle = endAngle;
        this.attribute = attribute;
    }

    public GfxArc(String value)
    {
        String[] values = value.trim().split("\\s+");
        try
        {
            this.x = MathCommon.doubleStr2int(values[0]);
            this.y = MathCommon.doubleStr2int(values[1]);
            this.radius = MathCommon.doubleStr2int(values[2]);
            this.startAngle = MathCommon.doubleStr2int(values[3]);
            this.endAngle = MathCommon.doubleStr2int(values[4]);
            if (values.length > 5)
            {
                this.attribute = GfxEntityAttribute.resolveGfxEntityAttribute(values[5]);
            }
        }
        catch (Exception e)
        {
            System.out.println("***" + value + "***");
            e.printStackTrace();
        }
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getRadius()
    {
        return radius;
    }

    public int getStartAngle()
    {
        return startAngle;
    }

    public int getEndAngle()
    {
        return endAngle;
    }

    public GfxEntityAttribute getAttribute()
    {
        return attribute;
    }

    public String toString()
    {
        return String.format("arc %3d %3d %3d %3d %3d", this.getX(), this.getY(), this.getRadius(), this.startAngle, this.endAngle);
    }

    public void draw(Graphics2D gc)
    {
        int x = this.getX()-this.getRadius();
        int y = this.getY()-this.getRadius();
        int width = this.getRadius() << 1;
        int height = width;
        int startAngle = this.startAngle;
        int arcAngle = this.getEndAngle() - this.getStartAngle();
        if (arcAngle < 0)
        {
            arcAngle += 360;
        }
        gc.drawArc(x, y, width, height, startAngle, arcAngle);
    }
}
