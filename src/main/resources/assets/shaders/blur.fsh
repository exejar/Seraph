#version 120

uniform sampler2D u_texture;

uniform vec2 u_texelSize;

uniform float u_blurRadius;
uniform vec4 u_location;
uniform vec4 u_rectRadius;
uniform float u_pass;

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

    if (u_pass == 1 || u_pass == 2) {
        for (float f = -u_blurRadius; f <= u_blurRadius; f++) {
            vec2 direction = vec2(float(u_pass == 1), float(u_pass == 2));
            color += texture2D(u_texture, texCoord + f * u_texelSize * direction) * gauss(f, u_blurRadius / 2);
        }
        gl_FragColor = vec4(color.rgb, 1.0);
    } else if (u_pass == 3) {
        float distance = roundedBoxSDF(gl_FragCoord.xy - u_location.xy - u_location.zw, u_location.zw, u_rectRadius);

        if (distance < 0.0) {
            gl_FragColor = texture2D(u_texture, texCoord);
        }
    }
}