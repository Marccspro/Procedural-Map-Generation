#version 330 core

out vec4 out_color;

uniform sampler2D tex;
in vec2 v_texcoord;

void main()
{
    out_color = texture(tex, v_texcoord);
}
