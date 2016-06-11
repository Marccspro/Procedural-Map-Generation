#version 330
#include "light.glsl"

out vec4 out_color;

uniform sampler2D diffuse_sampler;
uniform sampler2D normal_sampler;
uniform sampler2D shadow_sampler;

uniform int light_only;

in vec3 v_position;
in vec2 v_coord;
in vec3 v_normal;
in vec3 v_tangent;
in vec3 v_biTangent;

in vec4 mvpPosition;
in mat3 tbnMatrix;
in vec4 lightMvpPosition;

uniform DirectionalLight light;

void main(void)
{
    vec4 texture_color = texture(diffuse_sampler, v_coord);
    if (use_texture == 0)
        texture_color = vec4(1, 0, 1, 1);
    vec3 final_normal = v_normal;
    if (use_normal == 1)
        final_normal = calc_normal_map(tbnMatrix, normal_sampler, v_coord);
    float shadow_factor = calc_shadow_mapping(lightMvpPosition, shadow_sampler);
    vec4 light_color = calc_dir_light(light, final_normal) * shadow_factor;
    vec4 final_color = texture_color * light_color;

    if (final_color.a < 0.5)
        discard;

    out_color = final_color;

    if (light_only == 1)
	    out_color = light_color;
}