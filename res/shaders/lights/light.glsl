#define AMBIANT_LIGHT 0
#define DIRECTIONAL_LIGHT 1

uniform vec3 camera_position;

uniform float specular_intensity;
uniform float specular_power;

uniform int use_texture;
uniform int use_normal;

in vec3 world_position;

struct Light
{
    vec4 color;
    float intensity;
};

struct DirectionalLight
{
    Light light;
    vec3 direction;
};

vec3 calc_normal_map(mat3 tbnMatrix, sampler2D normal_sampler, vec2 tex_coord)
{
    vec3 normal_texture = 2.0 * texture(normal_sampler, tex_coord).rgb - 1.0;
    return normalize(tbnMatrix * normal_texture);
}

float calc_shadow_mapping(vec4 lightMvpPosition, sampler2D shadow_sampler)
{
    vec3 screen_coord = (lightMvpPosition.xyz / lightMvpPosition.w) * 0.5 + 0.5;
    if (screen_coord.x <= 0 || screen_coord.x >= 1 ||
        screen_coord.y <= 0 || screen_coord.y >= 1 ||
        screen_coord.z <= 0 || screen_coord.z >= 1)
        return 1.0;


    float depth = screen_coord.z;
    float shadow_depth = texture(shadow_sampler, screen_coord.xy).x;

    if (depth < shadow_depth + 0.001)
        return 1.0;
    return 0.0;
}

vec4 calc_light(Light light, vec3 direction, vec3 normal)
{
    float diffuse_factor = dot(normal, direction);
    vec4 diffuse_color = vec4(0.0);
    vec4 specular_color = vec4(0.0);

    if (diffuse_factor > 0)
       diffuse_color = vec4(light.color.rgb * light.intensity * diffuse_factor, 1.0);

    vec3 direction_to_eye = normalize(camera_position - world_position);
    vec3 reflection_direction = normalize(reflect(-direction, normal));

    float specular_factor = pow(dot(direction_to_eye, reflection_direction), specular_power) * clamp(0, 1, diffuse_factor);

    if (specular_factor > 0)
        specular_color = vec4(light.color.rgb * specular_intensity * specular_factor, 1.0);

    return  vec4(diffuse_color.rgb + specular_color.rgb, 1);
}

vec4 calc_dir_light(DirectionalLight light, vec3 normal)
{
    return  calc_light(light.light, light.direction, normal);
}
