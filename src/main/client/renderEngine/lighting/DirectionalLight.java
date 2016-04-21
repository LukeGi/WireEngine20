package main.client.renderEngine.lighting;

import main.coreEngine.util.maths.Vector3f;

/**
 * Created by Kelan on 15/04/2016.
 */
public class DirectionalLight extends AbstractLight
{
    public DirectionalLight(Vector3f direction, Vector3f colour, float intensity)
    {
        super(TYPE_DIRECTION, colour, intensity);
        this.direction = direction;
    }
}