#version 120
uniform vec2 u_resolution;
uniform vec2 u_location;
uniform vec4 u_radius;
uniform vec4 u_color;

float roundedBoxSDF(vec2 pos, vec2 size, vec4 radius) {
    radius.xy = (pos.x > 0.0) ? radius.xy : radius.zw;
    radius.x  = (pos.y > 0.0) ? radius.x : radius.y;
    vec2 q = abs(pos) - size + radius.x;
    return min(max(q.x, q.y), 0.0) + length(max(q, 0.0)) - radius.x;
}

void main() {
    float edgeSoftness = 2.0F;

    float distance = roundedBoxSDF(gl_FragCoord.xy - u_location - u_resolution, u_resolution, u_radius);
    float smoothedAlpha =  u_color.a - smoothstep(0.0, edgeSoftness, distance);
    vec4 color = mix(vec4(0.0F), u_color, smoothedAlpha);

    gl_FragColor = color;
}