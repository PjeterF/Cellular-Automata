package org.graphics;

import org.lwjgl.opengl.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {
    private int ID;

    public ShaderProgram(String vertexPath, String fragmentPath){
        String vertexSource=readFile(vertexPath);
        int vertexShader=glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexSource);
        glCompileShader(vertexShader);

        if(glGetShaderi(vertexShader, GL_COMPILE_STATUS)==GL_FALSE){
            String error="Error compiling vertex shader:\n"+glGetShaderInfoLog(vertexShader);
            glDeleteShader(vertexShader);
            ID=-1;
            throw new RuntimeException(error);
        }

        String fragmentSource=readFile(fragmentPath);
        int fragmentShader=glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentSource);
        glCompileShader(fragmentShader);

        if(glGetShaderi(fragmentShader, GL_COMPILE_STATUS)==GL_FALSE) {
            String error="Error compiling fragment shader:\n" + glGetShaderInfoLog(vertexShader);
            glDeleteShader(fragmentShader);
            ID = -1;
            throw new RuntimeException(error);
        }

        ID=glCreateProgram();
        glAttachShader(ID, vertexShader);
        glAttachShader(ID, fragmentShader);
        glLinkProgram(ID);

        if(glGetProgrami(ID, GL_LINK_STATUS)==GL_FALSE){
            String error="Error linking shader program:\n" + glGetProgramInfoLog(ID);
            glDeleteProgram(ID);
            ID = -1;
            throw new RuntimeException(error);
        }

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }

    public int getID(){
        return ID;
    }

    private String readFile(String path){
        try {
            Scanner scanner=new Scanner(getClass().getClassLoader().getResourceAsStream(path));
            StringBuilder builder= new StringBuilder();

            while(scanner.hasNextLine()){
                builder.append(scanner.nextLine());
                builder.append("\n");
            }

            return builder.toString();
        } catch (NullPointerException  e) {
            throw new RuntimeException("Shader file not found in resources");
        }
    }

    protected void terminate(){
        glDeleteProgram(ID);
    }
}
