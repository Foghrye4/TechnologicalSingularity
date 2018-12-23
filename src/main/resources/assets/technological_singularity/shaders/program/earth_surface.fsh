#version 120

uniform sampler2D DiffuseSampler;
uniform sampler2D earth_night_texture;
uniform sampler2D earth_day_texture;
uniform vec3 directionToSun;

varying vec2 texCoord;

void main(){
//    vec4 color = texture2D(earth_day_texture, texCoord);
//    gl_FragColor = vec4(color.rgb, 1.0);
    
    
    vec4 diffuseColor = texture2D(DiffuseSampler, texCoord);
    gl_FragColor = vec4(diffuseColor.rgb, 1.0);
}