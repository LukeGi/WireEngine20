#version 400 core

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

in vec3 vertPosition;
in vec3 vertNormal;
in vec2 vertTexture;
in vec4 vertColour;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
uniform vec3 cameraPos;
uniform Light light;

out vec3 pVertPosition;
out vec3 pVertNormal;
out vec2 pVertTexture;
out vec4 pVertColour;
out vec3 pDirToLight;

void main()
{
    vec4 transformedVertex = modelMatrix * vec4(vertPosition, 1.0);

    pVertPosition = transformedVertex.xyz;
    pVertNormal = (modelMatrix * vec4(vertNormal, 0.0)).xyz;
    pVertTexture = vertTexture;
    pVertColour = vertColour;

    pDirToLight = light.position - transformedVertex.xyz;
    gl_Position = projectionMatrix * viewMatrix * transformedVertex;
}
