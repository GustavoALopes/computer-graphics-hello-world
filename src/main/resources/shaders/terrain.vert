#version 400 core

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;

layout(location=0) in vec3 position;
layout(location=1) in vec2 textCoordsIn;
layout(location=2) in vec3 normalsIn;

out vec2 textCoordsOut;
out vec3 surfaceNormalOut;
out vec3 toLightVectorOut;
out vec3 toCameraVectorOut;

void main() {
        textCoordsOut = textCoordsIn * 40.0;

        vec4 worldPosition = transformationMatrix * vec4(position, 1.0f);
        gl_Position = projectionMatrix * viewMatrix * worldPosition;

        surfaceNormalOut = (transformationMatrix * vec4(normalsIn, 0.0)).xyz;
        toLightVectorOut = lightPosition - worldPosition.xyz;
        toCameraVectorOut = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;
}