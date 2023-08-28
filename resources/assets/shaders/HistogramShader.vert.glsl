#version 330 core

layout(location = 0) in vec3 color;
layout(location = 1) in float height;

uniform mat4 viewProjectionMatrix;
uniform int count;

out vec3 s_Color;

void main() {
    float x, h, a;
    a = 1 / float(count);

    switch (gl_VertexID) {
        case 0:
        x = 0;
        h = 0;
        break;
        case 1:
        x = a;
        h = 0;
        break;
        case 2:
        x = a;
        h = height;
        break;
        default :
        x = 0;
        h = height;
        break;
    }

    gl_Position = viewProjectionMatrix * vec4(x + gl_InstanceID * a, h, 0, 1);
    s_Color = color;
}
