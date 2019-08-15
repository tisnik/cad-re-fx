package cz.tisnik.cadgfx.gfxentity;

import java.awt.Graphics2D;

import cz.tisnik.cadgfx.utils.MathCommon;

public class GfxCircle implements GfxEntity
{
    private int x;
    private int y;
    private int radius;
    private GfxEntityAttribute attribute;

    public GfxCircle()
    {
        this(0, 0, 0, GfxEntityAttribute.UNKNOWN);
    }

    public GfxCircle(int x, int y, int radius, GfxEntityAttribute attribute)
    {
        super();
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.attribute = attribute;
    }

    public GfxCircle(String value)
    {
        String[] values = value.trim().split("\\s+");
        try
        {
            this.x = MathCommon.doubleStr2int(values[0]);
            this.y = MathCommon.doubleStr2int(values[1]);
            this.radius = MathCommon.doubleStr2int(values[2]);
            if (values.length > 3)
            {
                this.attribute = GfxEntityAttribute.resolveGfxEntityAttribute(values[3]);
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

    public GfxEntityAttribute getAttribute()
    {
        return attribute;
    }

    public String toString()
    {
        return String.format("circle %3d %3d %3d", this.getX(), this.getY(), this.getRadius());
    }

    public void draw(Graphics2D gc)
    {
        int size = this.getRadius() << 1;
        gc.drawOval(this.getX() - this.getRadius(), this.getY() - this.getRadius(), size, size);
    }
}
