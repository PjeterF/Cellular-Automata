package org.automata.storage;

import org.automata.elements.ElementBase;
import org.automata.elements.Vacuum;

public class WorldGenerator {
    private WorldGenerator(){

    }

    public static void empty(World world){
        for(int x=0;x<world.width;x++){
            for(int y=0;y<world.height;y++){
                world.setElement(x, y, new Vacuum());
            }
        }
    }
}
