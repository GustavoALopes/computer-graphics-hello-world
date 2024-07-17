#version 400 core

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;

uniform float useFakeLighting;

layout(location=0) in vec3 position;
layout(location=1) in vec3 colorIn;
layout(location=2) in vec2 textCoordsIn;
layout(location=3) in vec3 normalsIn;

out vec3 colorOut;
out vec2 textCoordsOut;
out vec3 surfaceNormalOut;
out vec3 toLightVectorOut;
out vec3 toCameraVectorOut;

void main() {
        colorOut = colorIn;
        textCoordsOut = textCoordsIn;

        vec4 worldPosition = transformationMatrix * vec4(position, 1.0f);
        gl_Position = projectionMatrix * viewMatrix * worldPosition;

        vec3 currentNormals = mix(normalsIn, vec3(0, 1, 0), useFakeLighting);

        surfaceNormalOut = (transformationMatrix * vec4(currentNormals, 0.0)).xyz;
        toLightVectorOut = lightPosition - worldPosition.xyz;
        toCameraVectorOut = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;
}