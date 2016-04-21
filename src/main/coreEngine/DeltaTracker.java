package main.coreEngine;

/**
 * Created by Kelan on 02/04/2016.
 */
public class DeltaTracker
{
    public long lastTime;
    public double delta;

    public long getLastTime()
    {
        return lastTime;
    }

    public void setLastTime(long lastTime)
    {
        this.lastTime = lastTime;
    }

    public double getDelta()
    {
        return delta;
    }

    public void setDelta(double delta)
    {
        this.delta = delta;
    }
}
