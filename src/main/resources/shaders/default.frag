#version 330 core

in vec3 colorOut;

out vec4 fragColor;

void main() {
    fragColor = vec4(colorOut, 1.0f);//vec4(0.0f, 1.0f, 0.0f, 1.0f);
}