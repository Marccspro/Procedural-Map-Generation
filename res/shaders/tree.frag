#version 330 core
#include "lights/light.glsl"

out vec4 out_color;

uniform sampler2D textureSampler;
uniform int black;

in vec3 world_pos;
in vec3 v_normal;
in vec2 v_texcoord;

uniform Light light;

void main(void)
{
    vec4 texture_color = texture(textureSampler, v_texcoord);
    if (texture_color.a < 0.5)
        discard;

    vec3 light_color = texture_color.rgb * calc_light(light, v_normal).rgb;

    if (black != 1)
        out_color = vec4(light_color, 1);
    else
        out_color = vec4(0, 0, 0, 1);
}