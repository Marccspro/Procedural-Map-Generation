#version 330 core

layout (location = 0) in vec3 in_position;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

out vec3 world_pos;
out vec3 v_position;

void main(void)
{
    world_pos = (modelViewMatrix * vec4(in_position, 1.0)).xyz;
    v_position = in_position;
    gl_Position = projectionMatrix * modelViewMatrix * vec4(in_position, 1.0);
}