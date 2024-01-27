#version 400 core

in vec3 colorOut;
in vec2 textCoordsOut;
in vec3 surfaceNormalOut;
in vec3 toLightVectorOut;

out vec4 fragColor;

uniform sampler2D textureSampler;
uniform vec3 lightColor;

void main() {
    vec3 normalizedNormalsVector = normalize(surfaceNormalOut);
    vec3 normalizedLightPosition = normalize(toLightVectorOut);

    float dotProduct = dot(normalizedNormalsVector, normalizedLightPosition);

    float brightness = max(dotProduct, 0.0);
    vec3 difusse = brightness * lightColor;

    fragColor = vec4(difusse, 1.0) * texture(textureSampler, textCoordsOut);
}