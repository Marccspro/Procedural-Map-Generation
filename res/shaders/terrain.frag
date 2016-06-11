#version 330 core
#include "lights/light.glsl"

out vec4 out_color;

uniform sampler2D grass_texture;
uniform sampler2D shadow_map;

uniform int black;

in vec4 projection;
in vec3 world_pos;
in vec3 v_normal;
in vec2 v_texcoords;

uniform Light light;

void main(void)
{
    vec3 screen_coord = (projection.xyz / projection.w) * 0.5 + 0.5;
    float depth = screen_coord.z;
    float shadow_depth = texture(shadow_map, screen_coord.xy).x;

    vec3 texture_color = texture(grass_texture, v_texcoords).rgb;
    vec3 light_color = calc_light(light, v_normal).rgb;

    vec3 final_color = texture_color;

    if (depth > shadow_depth + 0.001)
        final_color *= light_color;

    if (black != 1)
        out_color = vec4(final_color, 1);
    else
        out_color = vec4(0, 0, 0, 1);
}