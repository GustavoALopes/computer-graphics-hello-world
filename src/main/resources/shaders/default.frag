#version 400 core

in vec3 colorOut;
in vec2 textCoordsOut;

out vec4 fragColor;

uniform sampler2D textureSampler;

void main() {
    fragColor = texture(textureSampler, textCoordsOut);
    //vec4(colorOut, 1.0f);//vec4(0.0f, 1.0f, 0.0f, 1.0f);
}