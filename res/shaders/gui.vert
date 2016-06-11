#version 330 core

layout (location = 0) in vec3 in_position;
layout (location = 1) in vec2 in_texcoord;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

out vec2 v_texcoord;

void main() {
    v_texcoord = in_texcoord;
	gl_Position = projectionMatrix * modelViewMatrix * vec4(in_position, 1.0);
}
