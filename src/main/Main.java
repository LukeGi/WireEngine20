package main;

import main.coreEngine.Engine;
import main.game.TestGame;

/**
 * Created by Kelan on 01/04/2016.
 */
public class Main
{
    public static void main(String[] args)
    {
        Engine.addGame(new TestGame());
        Engine.start();
    }
}