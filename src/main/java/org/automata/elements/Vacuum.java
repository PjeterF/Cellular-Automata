package org.automata.elements;

import org.automata.storage.World;

public class Vacuum extends ElementBase{
    public Vacuum(){
        super(0.8294f, 1.0f, 1.0f, 1.0f, ElementType.VACUUM, ElementCategory.GAS);
    }

    @Override
    public void update(int x, int y, World world) {

    }
}
