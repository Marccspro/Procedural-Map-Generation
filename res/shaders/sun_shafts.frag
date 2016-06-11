#version 330

out vec4 out_color;

uniform sampler2D tex;
in vec2 v_texcoord;
in vec4 sun;

void main()
{
// --------------- Works but slow -----------------
//
    int iteration = 100;
    vec2 sun_screen_pos = (sun.xy / sun.w) * 0.5 + 0.5;
    vec2 dir = (sun_screen_pos - v_texcoord);

    vec4 color = texture(tex, v_texcoord);

	for (int i = 0; i < iteration; i++)
	{
        color += texture(tex, v_texcoord + dir * 0.01 * i);
	}
	color /= iteration;
    out_color = vec4(1, 0.8, 0.3, color.r);
}
