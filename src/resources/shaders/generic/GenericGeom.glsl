#version 400 core

const float PI = 3.1415926;
struct Light
{
    vec3 position;
    vec3 attenuation;
    vec3 direction;
    vec3 colour;
    float intensity;
    float coneCutoff;
    int type;
};

layout(triangles) in;
layout(triangle_strip) out;
layout(max_vertices = 64) out;

in vec3 pVertPosition[3];
in vec3 pVertNormal[3];
in vec2 pVertTexture[3];
in vec4 pVertColour[3];
in vec3 pDirToLight[3];
in vec3 pCameraPos[3];

uniform bool useFaceColours;
uniform vec3 cameraPos;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform float minBrightness;
uniform Light light;

out vec3 ppVertPosition;
out vec3 ppVertNormal;
out vec2 ppVertTexture;
out vec4 ppVertColour;
out vec3 ppDirToLight;

void createCircle(vec3 xAxis, vec3 yAxis, vec3 zAxis, vec3 pos, float resolution, float radius, vec3 colour)
{
    xAxis = normalize(xAxis);
    yAxis = normalize(yAxis);
    zAxis = normalize(zAxis);

    for (int i = 0; i <= resolution; i++)
    {
        float ang = PI * 2.0 / resolution * i;
        float cosAng = cos(ang);
        float sinAng = sin(ang);

        float x = zAxis.x + radius * cosAng * xAxis.x + radius * sinAng * yAxis.x;
        float y = zAxis.y + radius * cosAng * xAxis.y + radius * sinAng * yAxis.y;
        float z = zAxis.z + radius * cosAng * xAxis.z + radius * sinAng * yAxis.z;

        vec4 offset = vec4(x, y, z, 0.0);

        gl_Position = projectionMatrix * viewMatrix * (vec4(pos, 1.0) + offset);
        ppVertColour = vec4(colour, 0.0);
        EmitVertex();

        gl_Position = projectionMatrix * viewMatrix * (vec4(pos, 1.0));
        ppVertColour = vec4(colour, 1.0);
        EmitVertex();
    }

    EndPrimitive();
}

void main()
{
    for (int i = 0; i < 3; i++)
    {
        ppVertPosition = pVertPosition[i];
        ppVertNormal = pVertNormal[i];
        ppVertTexture = pVertTexture[i];
        ppVertColour = pVertColour[i];
        ppDirToLight = pDirToLight[i];
        gl_Position = gl_in[i].gl_Position;

        EmitVertex();
    }
    EndPrimitive();

    vec3 z = normalize(cameraPos - light.position);
    vec3 x = cross(z, vec3(0.0, 1.0, 0.0));
    vec3 y = cross(x, z);
    createCircle(x, y, z, light.position, 20, 0.1, light.colour * light.intensity);

}
