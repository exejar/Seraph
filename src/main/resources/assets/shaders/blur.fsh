#version 120

uniform sampler2D u_texture;

uniform vec2 u_texelSize;
uniform vec2 u_direction;

uniform float u_radius;

#define step u_texelSize * u_direction

float gauss(float x, float sigma) {
    float pow = x / sigma;
    return (1.0 / (abs(sigma) * 2.50662827463) * exp(-0.5 * pow * pow));
}

void main() {
    vec4 color = vec4(0);
    vec2 texCoord = gl_TexCoord[0].st;

    for (float f = -u_radius; f <= u_radius; f++) {
        color += texture2D(u_texture, texCoord + f * u_texelSize * u_direction) * gauss(f, u_radius / 2);
    }

    gl_FragColor = vec4(color.rgb, 1.0);
}