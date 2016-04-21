package main.coreEngine;

import main.client.ClientThread;
import main.client.Window;
import main.coreEngine.sceneGraph.components.CameraComponent;
import main.server.ServerThread;
import sun.plugin.dom.exception.InvalidStateException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kelan on 01/04/2016.
 */
public final class Engine
{
    private static IGameHandler game;
    private static List<TickableThread> threads = new ArrayList<>();
    private static CameraComponent observer;

    public static IGameHandler getGame()
    {
        if (game == null)
        {
            System.err.println("TestGame was null");
            throw new RuntimeException("Invalid game");
        }
        return game;
    }

    public static void addGame(IGameHandler game)
    {
        Engine.game = game;

        try
        {
            initialiseGame(game);
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }

        game.getClientThread().start();
        game.getServerThread().start();
        game.getClientThread().invoke("preInit");
        game.getServerThread().invoke("preInit");
    }

    private static void initialiseGame(IGameHandler game) throws IllegalAccessException
    {
        if (game == null || game.getClass() == null)
            throw new InvalidStateException("Cannot initialise a null game.");

        Field client = null;
        Field server = null;
        Field observer = null;

        Class<?> c = game.getClass();

        while (c != null && (client == null || server == null))
        {
            for (Field field : c.getDeclaredFields())
            {
                if (field.isAnnotationPresent(Client.class) && field.isAnnotationPresent(Server.class))
                    throw new InvalidStateException("Client and server cannot share the same field declaration");

                if (field.isAnnotationPresent(Client.class))
                {
                    if (client == null)
                        client = field;
                    else
                        throw new InvalidStateException("Game can only have one client thread; multiple were found");
                } else if (field.isAnnotationPresent(Server.class))
                {
                    if (server == null)
                        server = field;
                    else
                        throw new InvalidStateException("Game can only have one server thread; multiple were found");
                }

                if (field.isAnnotationPresent(Observer.class))
                {
                    if (observer == null)
                        observer = field;
                }
            }
            c = c.getSuperclass();
        }

        if (client != null)
        {
            System.out.println("Located client for class " + c + ", initialising it");
            client.setAccessible(true);
            client.set(game, new ClientThread());
            addThread((TickableThread) client.get(game));
        } else
            throw new InvalidStateException("Failed to locate client thread for game class " + c + "; a client thread is required.");

        if (server != null)
        {
            server.setAccessible(true);
            server.set(game, new ServerThread());
            addThread((TickableThread) server.get(game));
        } else
            System.err.println("Failed to locate a server thread for game class " + c + "; server not required, continuing.");

        if (observer != null)
        {
            observer.setAccessible(true);
            observer.set(game, new CameraComponent());
            Engine.observer = (CameraComponent) observer.get(game);
        }
    }

    public static void start()
    {
        game.getClientThread().invoke("postInit");
        game.getServerThread().invoke("postInit");

        for (TickableThread t : threads)
        {
            t.deltaTracker.lastTime = System.nanoTime();
        }

        while (true)
        {
            for (TickableThread t : threads)
            {
                if (t.getTickrate() > 0)
                {
                    long now = System.nanoTime();

                    t.deltaTracker.delta += (now - t.deltaTracker.lastTime) / (1000000000.0D / t.getTickrate());
                    t.deltaTracker.lastTime = now;

                    if (t.deltaTracker.delta >= 1.0D)
                    {
                        t.scheduleTick();
                        t.deltaTracker.delta--;
                    }
                } else
                {
                    t.scheduleTick();
                }
            }
        }
    }

    public static void stop()
    {
        for (TickableThread t : threads)
            t.onGameClosed();

        for (TickableThread t : threads)
            t.interrupt();

        Window.destroyWindow();
    }

    public static void addThread(TickableThread thread)
    {
        threads.add(thread);
        while (!threads.contains(thread)) ;
    }

    public static CameraComponent getObserver()
    {
        return observer;
    }

    public static void getInput()
    {
        getGame().onInput();
    }
}
