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

const int TYPE_POINT = 0;
const int TYPE_SPOT = 1;
const int TYPE_DIRECTION = 2;
const float minAttenuation = 0.05;

in vec3 ppVertPosition;
in vec3 ppVertNormal;
in vec2 ppVertTexture;
in vec4 ppVertColour;
in vec3 ppDirToLight;
uniform sampler2D texture;
uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform float minBrightness;
uniform bool lightModel;

uniform int width;
uniform int height;

uniform Light light;
const bool renderLights = true;

void main()
{
    vec4 finalColour = vec4(0.0);
    vec2 screen = vec2(width, height);
    vec4 surfaceColour = ppVertColour * texture2D(texture, ppVertTexture);
    vec3 dirToLight = normalize(ppDirToLight);

    if (!lightModel)
        finalColour = surfaceColour;
    else
    {
        vec3 normal = normalize(ppVertNormal);
        float distToLight = length(ppDirToLight);

        vec3 ambient = surfaceColour.rgb * minBrightness;
        vec3 diffuse = vec3(0.0);
        vec3 specular = vec3(0.0);
        float attenuation = light.attenuation.x + light.attenuation.y * distToLight + light.attenuation.z * distToLight * distToLight;

        if (attenuation > 0.0)
            attenuation = 1.0 / attenuation;

        if (attenuation > minAttenuation)
        {
            float diffuseCoefficient = max(dot(normal, dirToLight), 0.0);
            diffuse = diffuseCoefficient * light.colour * surfaceColour.rgb;
        }

        finalColour = vec4(ambient + attenuation * (diffuse + specular), surfaceColour.a);
    }

    gl_FragColor = finalColour;
}
