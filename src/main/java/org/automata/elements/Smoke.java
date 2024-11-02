package org.automata.elements;

import org.automata.storage.Vector2D;
import org.automata.storage.World;
import org.utility.RandomNumberGenerator;

import java.util.function.Predicate;

public class Smoke extends ElementBase{
    private int lifespan=1000;

    static{
        float[][] colors = {
                {0.212f, 0.196f, 0.196f, 1.0f},
                {0.345f, 0.329f, 0.325f, 1.0f},
                {0.478f, 0.451f, 0.420f, 1.0f},
                {0.675f, 0.651f, 0.588f, 1.0f},
                {0.580f, 0.557f, 0.506f, 1.0f},
                {0.8f, 0.8f, 0.8f, 1.0f}
        };
        colorMap.put(ElementType.SMOKE, colors);
    }

    static int maxDistance=1;

    public Smoke(){
        super(
                colorMap.get(ElementType.SMOKE)[RandomNumberGenerator.getInstance().randomInt(0, colorMap.get(ElementType.SMOKE).length-1)],
                ElementType.SMOKE,
                ElementCategory.GAS
        );
    }

    @Override
    public void update(int x, int y, World world) {
        try{
            lifespan--;
            if(lifespan<0){
                world.setElement(x, y, new Vacuum());
                return;
            }

            Predicate<ElementBase> stopCondition= element -> {
                if(element.category==ElementCategory.SOLID){
                    return true;
                }
                if(element.category==ElementCategory.LIQUID){
                    return true;
                }
                return false;
            };

            int d=RandomNumberGenerator.getInstance().randomInt(0, maxDistance);
            Vector2D<Integer> targetPosition=new Vector2D<>(x, y);
            switch (RandomNumberGenerator.getInstance().randomInt(0, 6)){
                case 0->{targetPosition.x-=d;}
                case 1->{targetPosition.x+=d;}
                case 2->{targetPosition.y-=d;}
                default->{targetPosition.y+=d;}
            }

            Vector2D<Integer> finalPosition=world.canMoveTo(new Vector2D<>(x, y), targetPosition, stopCondition);
            if(world.getElement(finalPosition.x, finalPosition.y).category==ElementCategory.SOLID){
                return;
            }

            world.switchElements(x, y, finalPosition.x, finalPosition.y);
        } catch (Exception e) {

        }
    }
}
