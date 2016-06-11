#version 330 core

layout (location = 0) in vec3 in_position;
layout (location = 1) in vec3 in_normal;
layout (location = 2) in vec2 in_texcoord;
layout (location = 3) in vec3 in_tengant;
layout (location = 4) in vec3 in_instance_position;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

out vec3 world_pos;
out vec3 v_normal;
out vec2 v_texcoord;

void main(void)
{
    world_pos = (modelViewMatrix * vec4(in_position + in_instance_position, 1.0)).xyz;
    v_normal = normalize(in_normal);
    v_texcoord = in_texcoord;
    gl_Position = projectionMatrix * modelViewMatrix * vec4(in_position + in_instance_position, 1.0);
}