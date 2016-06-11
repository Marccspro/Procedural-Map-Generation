#version 330 core

out vec4 out_color;

uniform samplerCube textureSampler;

in vec3 world_pos;
in vec3 v_position;

void main(void)
{
    vec4 texture_color = textureCube(textureSampler, v_position);
    if (texture_color.a < 0.5)
        discard;

    vec3 light_color = texture_color.rgb;
    out_color = vec4(light_color, 1);
}