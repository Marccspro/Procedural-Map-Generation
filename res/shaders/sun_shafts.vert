#version 330

layout (location = 0) in vec3 in_position;
layout (location = 1) in vec2 in_texcoord;

uniform mat4 sun_projection;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

out vec4 sun;
out vec2 v_texcoord;

void main() {
    sun = sun_projection * vec4(0, 0, 0, 1.0);
    v_texcoord = in_texcoord;
	gl_Position = projectionMatrix * modelViewMatrix * vec4(in_position, 1.0);
}
