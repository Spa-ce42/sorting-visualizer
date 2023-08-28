#version 330 core

in vec3 s_Color;
out vec4 color;

void main() {
    color = vec4(s_Color, 1);
}
