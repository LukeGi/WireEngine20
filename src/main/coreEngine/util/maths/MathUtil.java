package main.coreEngine.util.maths;

/**
 * Created by Kelan on 10/04/2016.
 */
public class MathUtil
{
    public static float cotf(float radians)
    {
        return (float) (1.0F / Math.tan(radians));
    }

    public static double cotd(double radians)
    {
        return 1.0 / Math.tan(radians);
    }

    public static float toRadiansf(float degrees)
    {
        return (float) Math.toRadians(degrees);
    }
}
