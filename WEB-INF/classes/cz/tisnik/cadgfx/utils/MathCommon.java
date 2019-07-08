package cz.tisnik.cadgfx.utils;

public class MathCommon
{
    public static int double2int(double value)
    {
        return (int)Math.round(value);
    }

    public static int doubleStr2int(String value)
    {
        return double2int(Double.parseDouble(value.trim()));
    }
}
