#version 120

uniform sampler2D earth_night_texture;
uniform sampler2D earth_day_texture;
uniform vec3 directionToSun;

attribute vec4 Position;

uniform mat4 ProjMat;
uniform vec2 InSize;
uniform vec2 OutSize;

varying vec2 texCoord;

void main(){
    vec4 outPos = ProjMat * vec4(Position.xy, 0.0, 1.0);
    gl_Position = vec4(outPos.xy, 0.2, 1.0);

//    gl_Position = gl_Vertex;
    texCoord = gl_MultiTexCoord0.xy;
}