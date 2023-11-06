#version 330 core

uniform mat4 Matrix;

layout(location=0) in vec3 position;
layout(location=1) in vec3 colorIn;
layout(location=2) in vec2 textCoordsIn;

out vec3 colorOut;
out vec2 textCoordsOut;

void main() {
        colorOut = colorIn;
        textCoordsOut = textCoordsIn;
        gl_Position = Matrix*vec4(position, 1.0f);
}