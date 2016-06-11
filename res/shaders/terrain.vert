#version 330 core

layout (location = 0) in vec3 in_position;
layout (location = 1) in vec3 in_normal;
layout (location = 2) in vec2 in_texcoords;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;
uniform mat4 lightProjection;

out vec4 projection;
out vec3 world_pos;
out vec3 v_normal;
out vec2 v_texcoords;

void main(void)
{
    projection = lightProjection * vec4(in_position, 1.0);
    world_pos = (modelViewMatrix * vec4(in_position, 1.0)).xyz;
    v_normal = normalize(in_normal);
    v_texcoords = in_texcoords;
    gl_Position = projectionMatrix * modelViewMatrix * vec4(in_position, 1.0);
}