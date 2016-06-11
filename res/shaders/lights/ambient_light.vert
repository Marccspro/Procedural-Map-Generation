#version 330

layout (location = 0) in vec3 in_position;
layout (location = 1) in vec3 in_normal;
layout (location = 2) in vec2 in_coord;
layout (location = 3) in vec3 in_tangent;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

out vec3 v_position;
out vec2 v_coord;
out vec3 v_normal;
out vec3 v_tangent;
out vec3 v_biTangent;

out vec3 world_position;
out vec4 mvpPosition;
out mat3 tbnMatrix;

void main() {
    v_position = in_position;
    v_coord = in_coord;
    v_normal = normalize((modelViewMatrix * vec4(in_normal, 0.0)).xyz);
    v_tangent = normalize((modelViewMatrix * vec4(in_tangent, 0.0)).xyz);
    v_biTangent = normalize(cross(v_normal, v_tangent));

    world_position = (modelViewMatrix * vec4(in_position, 0.0)).xyz;
    mvpPosition = projectionMatrix * modelViewMatrix * vec4(in_position, 1.0);
    tbnMatrix = mat3(v_tangent, v_biTangent, v_normal);

	gl_Position = mvpPosition;
}
