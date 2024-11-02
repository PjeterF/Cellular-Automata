package org.graphics;

import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.opengl.GL30C.*;
import static org.lwjgl.opengl.GL43C.GL_SHADER_STORAGE_BUFFER;

public class CanvasRenderer {
    private int VAO, VBO, EBO;
    private ShaderProgram shaderProgram;
    private int SSBO;
    private int[] width, height;
    private float[] pixelData;
    private int zoom=1;
    private int zoomUniformLocation;

    public CanvasRenderer(int width, int height, ShaderProgram shaderProgram){
        this.shaderProgram=shaderProgram;
        this.width = new int[]{width};
        this.height = new int[]{height};
        this.zoomUniformLocation=glGetUniformLocation(shaderProgram.getID(), "zoom");

        float[] vertices={
                -1.0f, -1.0f,
                -1.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, -1.0f
        };

        int[] indices={
                0, 1, 2,
                0, 2, 3
        };

        pixelData=new float[width * height *4];

        VAO=glGenVertexArrays();
        glBindVertexArray(VAO);

        VBO=glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        EBO=glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 2, GL_FLOAT, false, 2*Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        SSBO=glGenBuffers();
        glBindBuffer(GL_SHADER_STORAGE_BUFFER, SSBO);
        glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 0, SSBO);
        glBufferData(GL_SHADER_STORAGE_BUFFER,  4*Integer.BYTES+4*Float.BYTES* height * width, GL_DYNAMIC_DRAW );

        glBindVertexArray(0);

        glUseProgram(shaderProgram.getID());
    }

    public void setZoom(int newZoom){
        if(newZoom<1){
            zoom=1;
        }else{
            zoom=newZoom;
        }
    }

    public int getWidth(){
        return width[0];
    }

    public int getHeight(){
        return height[0];
    }

    public void setPixelData(int x, int y, float r, float g, float b, float a){
        int index=4*(x+width[0]*y);
        pixelData[index+0]=r;
        pixelData[index+1]=g;
        pixelData[index+2]=b;
        pixelData[index+3]=a;
    }

    public void setPixelData(int x, int y, float rgba[]){
        int index=4*(x+width[0]*y);
        pixelData[index+0]=rgba[0];
        pixelData[index+1]=rgba[1];
        pixelData[index+2]=rgba[2];
        pixelData[index+3]=rgba[3];
    }

    public void draw(){
        this.bufferCanvasData();
        glUseProgram(shaderProgram.getID());
        glUniform1i(zoomUniformLocation, zoom);
        glBindVertexArray(VAO);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    public void terminate(){
        shaderProgram.terminate();
        glDeleteVertexArrays(VAO);
        glDeleteBuffers(VBO);
        glDeleteBuffers(EBO);
        glDeleteBuffers(SSBO);
    }

    private void bufferCanvasData(){
        glBindBuffer(GL_SHADER_STORAGE_BUFFER, SSBO);
        glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 0, SSBO);
        //glBufferData(GL_SHADER_STORAGE_BUFFER,  4*Integer.BYTES+4*Float.BYTES* height[0] * width[0], GL_DYNAMIC_DRAW );
        glBufferSubData(GL_SHADER_STORAGE_BUFFER, 0, this.width);
        glBufferSubData(GL_SHADER_STORAGE_BUFFER, Integer.BYTES, this.height);
        glBufferSubData(GL_SHADER_STORAGE_BUFFER, 4*Integer.BYTES, this.pixelData);
    }
}
