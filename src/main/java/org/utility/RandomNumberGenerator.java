package org.utility;

import java.util.Random;

public class RandomNumberGenerator {
    private Random random;
    static RandomNumberGenerator instance;

    public static RandomNumberGenerator getInstance(){
        if(instance==null){
            instance=new RandomNumberGenerator();
        }
        return instance;
    }

    private RandomNumberGenerator(){
        random=new Random();
    }

    public float generateFloat(float min, float max){
        return min+(max-min)*random.nextFloat();
    }

    public int randomInt(int min, int max){
        return random.nextInt(max + 1 - min) + min;
    }
}
