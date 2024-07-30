#version 400 core

in vec2 textCoordsOut;
in vec3 surfaceNormalOut;
in vec3 toLightVectorOut;
in vec3 toCameraVectorOut;

out vec4 fragColor;

uniform sampler2D backgroundColor;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMap;

uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;

void main() {

    vec4 blendMapColour = texture(blendMap, textCoordsOut);

    float backTextureAmount = 1 - (blendMapColour.r + blendMapColour.g + blendMapColour.b);
    vec2 tiledCoords = textCoordsOut * 40.0;

    vec4 backgroundTexturedColour = texture(backgroundColor, tiledCoords) * backTextureAmount;
    vec4 rTexturedColour = texture(rTexture, tiledCoords) * blendMapColour.r;
    vec4 gTexturedColour = texture(gTexture, tiledCoords) * blendMapColour.g;
    vec4 bTexturedColour = texture(bTexture, tiledCoords) * blendMapColour.b;

    vec4 totalColour = backgroundTexturedColour + rTexturedColour + gTexturedColour + bTexturedColour;

    vec3 normalizedNormalsVector = normalize(surfaceNormalOut);
    vec3 normalizedLightPosition = normalize(toLightVectorOut);

    float dotProduct = dot(normalizedNormalsVector, normalizedLightPosition);

    float brightness = max(dotProduct, 0.0);
    vec3 difusse = brightness * lightColor;

    vec3 normalizedCameraVector = normalize(toCameraVectorOut);
    vec3 lightDirection = -normalizedLightPosition;
    vec3 reflectedLightDirection = reflect(lightDirection, normalizedNormalsVector);

    float specularFactor = max(dot(reflectedLightDirection, normalizedCameraVector), 0.0);
    float dumpedFactor = pow(specularFactor, shineDamper);
    vec3 finalSpecular = dumpedFactor * reflectivity * lightColor;

    fragColor = totalColour;
}