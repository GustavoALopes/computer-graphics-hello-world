#version 400 core

in vec3 colorOut;
in vec2 textCoordsOut;
in vec3 surfaceNormalOut;
in vec3 toLightVectorOut;
in vec3 toCameraVectorOut;

out vec4 fragColor;

uniform sampler2D textureSampler;
uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;

void main() {
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

    vec4 textureColor = texture(textureSampler, textCoordsOut);
    if(textureColor.a < .5) {
        discard;
    }

    fragColor = vec4(difusse, 1.0) * textureColor + vec4(finalSpecular, 1.0);
}