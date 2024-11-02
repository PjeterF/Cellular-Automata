package org.application;

public class InputManager {
    private static InputManager instance;
    public boolean[] mouseKeyDown=new boolean[8];
    public boolean[] keyDown=new boolean[512];

    static public InputManager getInstance() {
        if(instance==null){
            instance=new InputManager();
        }
        return instance;
    }

    private InputManager(){

    }
}
