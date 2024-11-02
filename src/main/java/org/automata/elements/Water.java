package org.automata.elements;

import org.automata.storage.Vector2D;
import org.automata.storage.World;
import org.utility.RandomNumberGenerator;

import java.util.List;
import java.util.function.Predicate;

import static java.lang.Math.signum;

public class Water extends ElementBase {
    static{
        float colors[][] = {
                {0.0588f, 0.3686f, 0.6118f, 1.0f},
                {0.1373f, 0.5373f, 0.8549f, 1.0f},
                {0.1098f, 0.6392f, 0.9255f, 1.0f},
                {0.3529f, 0.7373f, 0.8471f, 1.0f},
                {0.4549f, 0.8f, 0.9569f, 1.0f}
        };
        colorMap.put(ElementType.WATER, colors);
    }
    private static int maxHorizontalDistance=5;
    private static int maxVerticalDistance=1;

    public Water(){
        super(
                colorMap.get(ElementType.WATER)[RandomNumberGenerator.getInstance().randomInt(0, colorMap.get(ElementType.WATER).length-1)],
                ElementType.WATER,
                ElementCategory.LIQUID
        );
    }

    @Override
    public void update(int x, int y, World world) {
        try{
            ElementBase otherElement=world.getElement(x, y-1);
            switch (otherElement.category){
                case GAS -> {
                    Predicate<ElementBase> stopCondition=element -> {
                        if(element.category==ElementCategory.SOLID){
                            return true;
                        }
                        return false;
                    };

                    Vector2D<Integer> finalTarget=world.canMoveTo(new Vector2D<Integer>(x, y), new Vector2D<Integer>(x, y-maxVerticalDistance), stopCondition);
                    world.switchElements(x, y, finalTarget.x, finalTarget.y);
                }
                case SOLID, LIQUID -> {
                    Predicate<ElementBase> stopCondition=element -> {
                        if(element.category==ElementCategory.SOLID){
                            return true;
                        }
                        return false;
                    };

                    int max=RandomNumberGenerator.getInstance().randomInt(-maxHorizontalDistance, maxHorizontalDistance);
                    Vector2D<Integer> finalTarget=world.canMoveTo(new Vector2D<Integer>(x, y), new Vector2D<Integer>(x+max, y), stopCondition);

                    ElementBase targetElement=world.getElement(finalTarget.x, finalTarget.y);
                    if(targetElement.category==ElementCategory.GAS){
                       world.switchElements(x, y, finalTarget.x, finalTarget.y);
                    }
                    if(targetElement.category==ElementCategory.LIQUID) {
                        if (RandomNumberGenerator.getInstance().randomInt(0, 100) < 1) {
                             world.switchElements(x, y, finalTarget.x, finalTarget.y);
                        }
                    }
                }
            }

        } catch (Exception e) {
            return;
        }
    }
}
