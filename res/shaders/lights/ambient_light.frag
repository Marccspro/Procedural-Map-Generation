#version 330
#include "light.glsl"

out vec4 out_color;

uniform sampler2D diffuse_sampler;
uniform sampler2D normal_sampler;

uniform int light_only;

in vec3 v_position;
in vec2 v_coord;
in vec3 v_normal;
in vec3 v_tangent;
in vec3 v_biTangent;

in vec4 mvpPosition;
in mat3 tbnMatrix;

uniform Light light;

void main()
{
    vec4 texture_color = texture(diffuse_sampler, v_coord);
    if (use_texture == 0)
        texture_color = vec4(1, 0, 1, 1);
    vec4 light_color = light.color * light.intensity;
    vec4 final_color = texture_color * vec4(light_color.rgb, 1.0);

    if (final_color.a < 0.5)
        discard;

    out_color = final_color;

    if (light_only == 1)
	    out_color = light_color;
}