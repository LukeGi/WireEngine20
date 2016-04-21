package main.client.renderEngine.lighting;

import main.coreEngine.util.maths.Vector3f;

/**
 * Created by Kelan on 15/04/2016.
 */
public class SpotLight extends AbstractLight
{
    public SpotLight(Vector3f position, Vector3f direction, Vector3f attenuation, Vector3f colour, float intensity, float coneCutoff)
    {
        super(TYPE_SPOT, colour, intensity);
        this.position = position;
        this.direction = direction;
        this.attenuation = attenuation;
        this.coneCutoff = coneCutoff;
    }
}
