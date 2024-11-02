package org.application;

import org.automata.elements.*;
import org.automata.storage.World;
import org.automata.storage.WorldGenerator;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.graphics.ShaderProgram;
import org.graphics.CanvasRenderer;
import org.utility.RandomNumberGenerator;

import java.util.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;

public class Application {
    private long window;
    private CanvasRenderer canvasRenderer;
    World world;
    int width, height;
    String name;
    int scale=5;
    int elementSelector=1;

    public Application(int width, int height, String name){
        this.width=width;
        this.height=height;
        this.name=name;

        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit()){
            throw new RuntimeException("Unable to initialize glfw");
        }

        window=glfwCreateWindow(width, height, name, 0, 0);
        if(window==0){
            throw new RuntimeException("Could not initialize glfw window");
        }

        glfwSetMouseButtonCallback(window, (window, button, action, mods)->{
            double[] x=new double[1];
            double[] y=new double[1];
            glfwGetCursorPos(window, x, y);
            y[0]=(double)height-y[0];

            int cellX=(int)x[0]/scale;
            int cellY=(int)y[0]/scale;

            switch (action){
                case GLFW_PRESS -> {
                    InputManager.getInstance().mouseKeyDown[button]=true;
                }
                case GLFW_RELEASE -> {
                    InputManager.getInstance().mouseKeyDown[button]=false;
                }
            }
        });

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            switch (action){
                case GLFW_PRESS -> {
                    InputManager.getInstance().keyDown[key]=true;

                    elementSelector=key-GLFW_KEY_0;
                }
                case GLFW_RELEASE -> {
                    InputManager.getInstance().keyDown[key]=false;
                }
            }
        });

        glfwMakeContextCurrent(window);
        glfwShowWindow(window);

        GL.createCapabilities();
    }

    public void getCursorPos(double[] x, double[] y){
        glfwGetCursorPos(window, x, y);
        y[0]=(double)height-y[0];
    }

    public void initialize(){
        try {
            canvasRenderer =new CanvasRenderer(width/scale, height/scale, new ShaderProgram("shaders/canvas.vert", "shaders/canvas.frag"));
            canvasRenderer.setZoom(scale);

            world=new World(canvasRenderer);
            WorldGenerator.empty(world);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void run(){
        double accumulator=0;
        double previousTime=System.nanoTime();
        double fps=60;
        double timeStep=1e9/fps;
        while(!glfwWindowShouldClose(window)){
            double newTime=System.nanoTime();
            double frameDuration=newTime-previousTime;
            previousTime=newTime;
            Double title=frameDuration/1e6;
            glfwSetWindowTitle(window, title.toString());

            if(frameDuration>timeStep*4){
                frameDuration=timeStep*4;
            }

            accumulator = accumulator + frameDuration;
            while(accumulator>=timeStep){
                //LOGIC UPDATE
                world.setElement(world.getWidth()/2+RandomNumberGenerator.getInstance().randomInt(-30, 30), world.getHeight()-1, new Sand());
                world.setElement(world.getWidth()/2+RandomNumberGenerator.getInstance().randomInt(-30, 30), world.getHeight()-1, new Water());
                world.updateMultithreaded(4);

                accumulator=accumulator-timeStep;
            }

            //DRAWING
            glClearColor(1, 1, 1, 1);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            world.draw();

            //INPUT
            if(InputManager.getInstance().mouseKeyDown[GLFW_MOUSE_BUTTON_1]){
                double[] x=new double[1];
                double[] y=new double[1];
                getCursorPos(x, y);
                x[0]=x[0]/scale;
                y[0]=y[0]/scale;

                double size=10;
                double halfSize=size/2;
                for(double X=x[0]-halfSize;X<x[0]+halfSize;X++){
                    for(double Y=y[0]-halfSize;Y<y[0]+halfSize;Y++){
                        switch (elementSelector){
                            case 0 ->{
                                world.setElement((int)X, (int)Y, new Vacuum());
                            }
                            case 1 ->{
                                world.setElement((int)X, (int)Y, new Stone());
                            }
                            case 2 ->{
                                world.setElement((int)X, (int)Y, new Water());
                            }
                            case 3 ->{
                                world.setElement((int)X, (int)Y, new Sand());
                            }
                            case 4 ->{
                                world.setElement((int)X, (int)Y, new Smoke());
                            }
                        }
                    }
                }
            }

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }
}
