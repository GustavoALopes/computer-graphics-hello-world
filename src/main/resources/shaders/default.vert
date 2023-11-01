#version 330 core

uniform mat4 Matrix;

layout(location=0) in vec3 position;

void main() {
        gl_Position = Matrix*vec4(position, 1.0f);
}