package org.automata.elements;

import org.automata.storage.World;
import org.utility.RandomNumberGenerator;

import java.util.Random;

public class Sand extends ElementBase{
    static{
        float colors[][]={
                {0.965f,0.843f,0.691f, 1.0f},
                {0.949f,0.824f,0.663f, 1.0f},
                {0.925f,0.800f,0.635f, 1.0f},
                {0.909f,0.769f,0.588f, 1.0f},
                {0.882f,0.749f,0.573f, 1.0f}
        };
        colorMap.put(ElementType.SAND, colors);
    }

    public Sand(){
        super(
                colorMap.get(ElementType.SAND)[RandomNumberGenerator.getInstance().randomInt(0, colorMap.get(ElementType.SAND).length-1)],
                ElementType.SAND,
                ElementCategory.SOLID
        );
    }

    @Override
    public void update(int x, int y, World world) {
        try{
            ElementBase otherElement=world.getElement(x, y-1);
            switch (otherElement.category){
                case GAS, LIQUID -> {
                    world.switchElements(x, y, x, y-1);
                    return;
                }
                case SOLID -> {
                    int offset=RandomNumberGenerator.getInstance().randomInt(-1, 1);
                    if(offset==0){
                        return;
                    }

                    ElementBase side=world.getElement(x+offset, y-1);
                    switch (side.category){
                        case GAS, LIQUID -> {
                            world.switchElements(x, y, x+offset, y-1);
                        }
                    }
                }
            }

        } catch (Exception e) {
            return;
        }
    }
}
