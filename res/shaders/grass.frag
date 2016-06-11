#version 330 core
#include "lights/light.glsl"

out vec4 out_color;

uniform sampler2D textureSampler;
uniform vec3 cameraPosition;
uniform int black;

in vec3 world_pos;
in vec3 v_normal;
in vec2 v_texcoord;

uniform Light light;

float rand(vec2 coord)
{
    return (cos(coord.x) + sin(coord.y)) / 4.0 + 0.5f;
}

void main(void)
{
    float dist = distance(cameraPosition, world_pos);
    if (dist > 50)
        discard;

    float smoothDiscard = clamp(0, 1, (dist - 10) / 40.0);

    float rand = rand(v_texcoord * 10000);
    if (rand < smoothDiscard)
        discard;

    vec4 texture_color = texture(textureSampler, v_texcoord);
    if (texture_color.a < 0.5)
        discard;

    vec4 light_color = texture_color * calc_light(light, v_normal);

    if (black != 1)
        out_color = light_color;
    else
        out_color = vec4(0, 0, 0, 1);
}