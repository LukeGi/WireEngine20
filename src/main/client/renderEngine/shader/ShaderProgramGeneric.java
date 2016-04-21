package main.client.renderEngine.shader;

import main.client.renderEngine.mesh.Mesh;

import static org.lwjgl.opengl.GL20.*;

/**
 * Created by Kelan on 06/04/2016.
 */
public class ShaderProgramGeneric extends AbstractShaderProgram
{
    public ShaderProgramGeneric(Shader... shaders)
    {
        super(shaders);
    }

    @Override
    public void setupUniforms()
    {
        glBindAttribLocation(program, Mesh.ATTRIBUTE_LOCATION_POSITION, "vertPosition");
        glBindAttribLocation(program, Mesh.ATTRIBUTE_LOCATION_NORMAL, "vertNormal");
        glBindAttribLocation(program, Mesh.ATTRIBUTE_LOCATION_TEXTURE, "vertTexture");
        glBindAttribLocation(program, Mesh.ATTRIBUTE_LOCATION_COLOUR, "vertColour");
    }
}
