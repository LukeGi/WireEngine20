package main.client.renderEngine.shader;

import main.client.renderEngine.lighting.AbstractLight;
import main.coreEngine.util.maths.*;
import org.lwjgl.BufferUtils;
import sun.plugin.dom.exception.InvalidStateException;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * Created by Kelan on 04/04/2016.
 */
public abstract class AbstractShaderProgram
{
    private Map<Integer, Shader> shaders = new HashMap<>();
    protected int program = 0;
    protected Map<String, Integer> uniformLocations = new HashMap<>();

    public AbstractShaderProgram(Shader... shaders)
    {
        program = glCreateProgram();

        for (Shader s : shaders)
        {
            s.loadShader(program);
            this.shaders.put(s.getType(), s);
        }

        setupUniforms();

        glLinkProgram(program);
        if (glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE)
            throw new InvalidStateException("Program failed to link\n\n" + glGetProgramInfoLog(program, glGetProgrami(program, GL_INFO_LOG_LENGTH)) + "\n\n");
        glValidateProgram(program);
        if (glGetProgrami(program, GL_VALIDATE_STATUS) == GL_FALSE)
            throw new InvalidStateException("Program failed to validate\n\n" + glGetProgramInfoLog(program, glGetProgrami(program, GL_INFO_LOG_LENGTH)) + "\n\n");
    }

    public void useProgram(boolean use)
    {
        glUseProgram(use ? program : 0);
    }

    public void setUniform(String uniform, Object value)
    {
        if (value == null)
        {
            throw new RuntimeException("Unable to set uniform " + uniform + " to null");
        }
        if (!uniformLocations.containsKey(uniform))
        {
            int location = glGetUniformLocation(program, uniform);

            if (location < 0)
                System.err.println("The uniform \"" + uniform + "\" was initialised to the location -1, the uniform may not exist, or is redundant.");

            uniformLocations.put(uniform, location);
        }

        int location = uniformLocations.get(uniform);

        if (value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long)
            glUniform1i(location, (int) value);

        else if (value instanceof Float || value instanceof Double)
            glUniform1f(location, (float) value);

        else if (value instanceof Boolean)
            glUniform1i(location, (boolean) value ? 1 : 0);

        else if (value instanceof Vector4f)
            glUniform4f(location, ((Vector4f) value).x, ((Vector4f) value).y, ((Vector4f) value).z, ((Vector4f) value).w);

        else if (value instanceof Vector3f)
            glUniform3f(location, ((Vector3f) value).x, ((Vector3f) value).y, ((Vector3f) value).z);

        else if (value instanceof Vector2f)
            glUniform2f(location, ((Vector2f) value).x, ((Vector2f) value).y);

        else if (value instanceof Matrix4f)
            glUniformMatrix4fv(location, false, ((Matrix4f) value).storeFlipped(BufferUtils.createFloatBuffer(4 * 4)));

        else if (value instanceof Matrix3f)
            glUniformMatrix3fv(location, false, ((Matrix3f) value).storeFlipped(BufferUtils.createFloatBuffer(3 * 3)));

        else if (value instanceof Matrix2f)
            glUniformMatrix2fv(location, false, ((Matrix2f) value).storeFlipped(BufferUtils.createFloatBuffer(2 * 2)));

        else if (value instanceof AbstractLight)
        {
            setUniform(uniform + "." + "position", ((AbstractLight) value).getPosition());
            setUniform(uniform + "." + "attenuation", ((AbstractLight) value).getAttenuation());
            setUniform(uniform + "." + "direction", ((AbstractLight) value).getDirection());
            setUniform(uniform + "." + "colour", ((AbstractLight) value).getColour());
            setUniform(uniform + "." + "intensity", ((AbstractLight) value).getIntensity());
            setUniform(uniform + "." + "coneCutoff", ((AbstractLight) value).getConeCutoff());
            setUniform(uniform + "." + "type", ((AbstractLight) value).getType());
        }
        else
            System.err.println("The uniform \"" + uniform + "\" as an unknown data type, or the object " + value.getClass() + " cannot be converted.");
    }

    public abstract void setupUniforms();

    public int getProgram()
    {
        return program;
    }
}
