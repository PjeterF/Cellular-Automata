#version 460

out vec4 finalColor;
uniform int zoom;

layout(std430, binding=0) buffer canvas{
    int width;
    int height;
    vec4 colors[];
};

void main(){
    int index=int(gl_FragCoord.x)/zoom+int(gl_FragCoord.y)/zoom*width;

    if(index>=width*height){
        finalColor=vec4(0.0, 0.0, 0.0, 0.0);
        return;
    }

    finalColor=colors[index];
}