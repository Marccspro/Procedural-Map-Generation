#version 330

out vec4 out_color;

uniform sampler2D light_texture;

void main()
{
    vec4 texture_color = texture(light_texture,  gl_TexCoord[0].st);
	if (texture_color.a < 0.5)
	    discard;
	out_color = vec4(1, 1, 1, 1);
}
