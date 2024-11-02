package org.automata.elements;

import org.automata.storage.World;
import org.utility.RandomNumberGenerator;

public class Stone extends ElementBase {
    static{
        float colors[][] = {
                {0.9373f, 0.9412f, 0.9451f, 1.0f},
                {0.7765f, 0.7490f, 0.7216f, 1.0f},
                {0.6196f, 0.6157f, 0.6118f, 1.0f},
                {0.7255f, 0.6118f, 0.5882f, 1.0f},
                {0.4098f, 0.3529f, 0.3765f, 1.0f}
        };
        colorMap.put(ElementType.STONE, colors);
    }

    public Stone(){
        super(
                colorMap.get(ElementType.STONE)[RandomNumberGenerator.getInstance().randomInt(0, colorMap.get(ElementType.STONE).length-1)],
                ElementType.STONE,
                ElementCategory.SOLID
        );
    }

    @Override
    public void update(int x, int y, World world) {

    }
}
