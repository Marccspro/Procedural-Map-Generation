#version 330

layout (location = 0) in vec3 in_position;
layout (location = 1) in vec3 in_instance_position;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

void main() {
    gl_TexCoord[0] = gl_MultiTexCoord0;
    gl_PointSize = 64;
	gl_Position = projectionMatrix * modelViewMatrix * vec4(in_position + in_instance_position, 1.0);
}
