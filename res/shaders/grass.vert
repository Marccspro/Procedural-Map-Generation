#version 330 core

layout (location = 0) in vec3 in_position;
layout (location = 1) in vec3 in_normal;
layout (location = 2) in vec2 in_texcoord;
layout (location = 3) in vec3 in_tengant;

layout (location = 4) in vec4 in_transform_0;
layout (location = 5) in vec4 in_transform_1;
layout (location = 6) in vec4 in_transform_2;
layout (location = 7) in vec4 in_transform_3;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

out vec3 world_pos;
out vec3 v_normal;
out vec2 v_texcoord;

void main(void)
{
    mat4 transformationMatrix = mat4(in_transform_0, in_transform_1, in_transform_2, in_transform_3);
    world_pos = (transformationMatrix * vec4(in_position, 1.0)).xyz;
    v_normal = normalize(in_normal);
    v_texcoord = in_texcoord;
    gl_Position = projectionMatrix * transformationMatrix * vec4(in_position, 1.0);
}