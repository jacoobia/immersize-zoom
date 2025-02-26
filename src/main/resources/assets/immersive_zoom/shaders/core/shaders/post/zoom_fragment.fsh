#version 150

uniform sampler2D DiffuseSampler;
uniform float ZoomFactor;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec2 center = vec2(0.5, 0.5);
    vec2 zoomedCoord = center + (texCoord - center) * ZoomFactor;
    fragColor = texture(DiffuseSampler, zoomedCoord);
}
