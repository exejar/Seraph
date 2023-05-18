#version 120

uniform sampler2D u_texture;

uniform vec2 u_texelSize;
uniform vec2 u_direction;

uniform float u_blurRadius;
// u_location.x = x, u_location.y = y, u_location.z = width, u_location.w = height
uniform vec4 u_location;
uniform vec4 u_rectRadius;

#define step u_texelSize * u_direction

float gauss(float x, float sigma) {
    float pow = x / sigma;
    return (1.0 / (abs(sigma) * 2.50662827463) * exp(-0.5 * pow * pow));
}

float roundedBoxSDF(vec2 pos, vec2 size, vec4 radius) {
    radius.xy = (pos.x > 0.0) ? radius.xy : radius.zw;
    radius.x  = (pos.y > 0.0) ? radius.x : radius.y;
    vec2 q = abs(pos) - size + radius.x;
    return min(max(q.x, q.y), 0.0) + length(max(q, 0.0)) - radius.x;
}

void main() {
    vec4 color = vec4(0);
    vec2 texCoord = gl_TexCoord[0].st;

    float distance = roundedBoxSDF(gl_FragCoord.xy - u_location.xy - u_location.zw, u_location.zw, u_rectRadius);

    if (distance < 0.0) {
        for (float f = -u_blurRadius; f <= u_blurRadius; f++) {
            color += texture2D(u_texture, texCoord + f * u_texelSize * u_direction) * gauss(f, u_blurRadius / 2);
        }
    } else {
        color = texture2D(u_texture, texCoord);
    }

    gl_FragColor = vec4(color.rgb, 1.0);
}