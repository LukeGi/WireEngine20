package main.coreEngine;

import javafx.util.Pair;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Kelan on 02/04/2016.
 */
public abstract class TickableThread extends Thread
{
    private float ups;

    public TickableThread(String name)
    {
        super(name);
        deltaTracker = new DeltaTracker();
    }

    private int ticks = 0;
    public DeltaTracker deltaTracker;
    private boolean running = false;
    private Set<Pair<String, Object[]>> methods = new HashSet<>();

    public final float getUpdateFrequency()
    {
        return ups;
    }

    @Override
    public final void run()
    {
        System.out.println("Starting thread " + Thread.currentThread().getName());
        running = true;

        while (true)
        {
            synchronized (methods)
            {
                for (Pair<String, Object[]> p : methods)
                {
                    try
                    {
                        System.out.println("Invoking method " + p.getKey() + " on thread " + Thread.currentThread().getName());
                        Class<?>[] paramTypes0 = new Class<?>[p.getValue().length];
                        Class<?>[] paramTypes1 = new Class<?>[p.getValue().length];

                        for (int i = 0; i < p.getValue().length; i++)
                        {
                            Object o = p.getValue()[i];

                            Class<?> class0 = o.getClass();
                            Class<?> class1 = o.getClass();

                            try
                            {
                                if (o instanceof Byte) class1 = byte.class;
                                if (o instanceof Short) class1 = short.class;
                                if (o instanceof Integer) class1 = int.class;
                                if (o instanceof Long) class1 = long.class;
                                if (o instanceof Character) class1 = char.class;
                                if (o instanceof Boolean) class1 = boolean.class;

                            } catch (Exception e)
                            {
                            }

                            paramTypes0[i] = class0;
                            paramTypes1[i] = class1;
                        }

                        Method m;

                        try
                        {
                            m = getClass().getMethod(p.getKey(), paramTypes0);
                        } catch (Exception e)
                        {
                            try
                            {
                                m = getClass().getMethod(p.getKey(), paramTypes1);
                            } catch (Exception e1)
                            {
                                e1.printStackTrace();
                                continue;
                            }
                        }

                        if (p.getValue() != null && p.getValue().length > 0)
                            m.invoke(this, p.getValue());
                        else
                            m.invoke(this);
                    } catch (Exception e)
                    {
                        System.err.println("An error occurred while invoking the method " + p.getKey() + " with the specified parameters");
                        e.printStackTrace();
                    }
                }
            }

            methods.clear();

            while (getTicks() > 0)
            {
                long start = System.nanoTime();
                tick();
                ticks--;

                long now = System.nanoTime();

                ups = 1000000000.0F / (float) (now - start);

            }
        }
    }

    public void scheduleTick()
    {
        synchronized (this)
        {
            ticks++;
        }
    }

    private synchronized int getTicks()
    {
        return ticks;
    }

    public final boolean isRunning()
    {
        return running;
    }

    protected abstract void tick();

    public abstract int getTickrate();

    public abstract void onGameClosed();

    @Override
    public synchronized void start()
    {
        super.start();

        while (!running) ;
    }

    public final void invoke(String method, Object... parameters)
    {
        Pair<String, Object[]> pair = new Pair<>(method, parameters);

        synchronized (methods)
        {
            methods.add(pair);
        }

        while (methods.contains(pair)) ;
    }
}
