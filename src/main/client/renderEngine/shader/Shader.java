package main.client.renderEngine.shader;

import sun.plugin.dom.exception.InvalidStateException;

import java.io.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.opengl.GL40.*;
import static org.lwjgl.opengl.GL43.*;

/**
 * Created by Kelan on 04/04/2016.
 */
public class Shader
{
    public static int VERTEX_SHADER = GL_VERTEX_SHADER;
    public static int TESS_CONTROL_SHADER = GL_TESS_CONTROL_SHADER;
    public static int TESS_EVALUATION_SHADER = GL_TESS_EVALUATION_SHADER;
    public static int GEOMETRY_SHADER = GL_GEOMETRY_SHADER;
    public static int FRAGMENT_SHADER = GL_FRAGMENT_SHADER;
    public static int COMPUTE_SHADER = GL_COMPUTE_SHADER;

    private String shaderName;
    private int type;
    private File source;
    private int id = -1;

    public Shader(int type, File source)
    {
        this.type = type;
        this.source = source;
        shaderName = getName(type);
    }

    public static String getName(int type)
    {
        if (type == VERTEX_SHADER) return "vertex";
        if (type == TESS_CONTROL_SHADER) return "tessellation";
        if (type == TESS_EVALUATION_SHADER) return "tessellation evaluation";
        if (type == GEOMETRY_SHADER) return "geometry";
        if (type == FRAGMENT_SHADER) return "fragment";
        if (type == COMPUTE_SHADER) return "compute";
        return "INVALID SHADER";
    }

    public int loadShader(int program)
    {
        int shader = glCreateShader(type);

        if (shader <= 0)
            return 0;

        glShaderSource(shader, readSource());
        glCompileShader(shader);

        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE)
            throw new InvalidStateException(this.shaderName.substring(0, 1).toUpperCase() + this.shaderName.substring(1) + " shader failed to parseOBJ [GL_COMPILE_STATUS = GL_FALSE]\n\n--------========OPENGL LOG INFO========--------\n\n" + glGetShaderInfoLog(shader, GL_INFO_LOG_LENGTH) + "\n\n--------========JAVA LOG INFO========--------\n");

        if (program != 0)
            glAttachShader(program, shader);
        else
            System.err.println("Successfully compiled shader, but program was not compiled. Shader will have to be linked manually.");

        return id = shader;
    }

    private String readSource()
    {
        StringBuilder source = new StringBuilder();

        try (FileInputStream in = new FileInputStream(this.source); InputStreamReader streamReader = new InputStreamReader(in, "UTF-8"); BufferedReader bufferedReader = new BufferedReader(streamReader))
        {
            String line;

            while ((line = bufferedReader.readLine()) != null)
                source.append(line).append("\n");


            bufferedReader.close();
            streamReader.close();
            in.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return source.toString();
    }

    public int getType()
    {
        return type;
    }

    public File getSource()
    {
        return source;
    }

    public int getId()
    {
        return id;
    }
}
