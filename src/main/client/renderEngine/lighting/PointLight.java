package main.client.renderEngine.lighting;

import main.coreEngine.util.maths.Vector3f;

/**
 * Created by Kelan on 15/04/2016.
 */
public class PointLight extends AbstractLight
{
    public PointLight(Vector3f position, Vector3f attenuation, Vector3f colour, float intensity)
    {
        super(TYPE_POINT, colour, intensity);
        this.position = position;
        this.attenuation = attenuation;
    }
}
