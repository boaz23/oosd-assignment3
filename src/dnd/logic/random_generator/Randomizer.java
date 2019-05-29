package dnd.logic.random_generator;

import java.util.Random;

public class Randomizer implements RandomGenerator {
    @Override
    public int nextInt(int n) {
        Random rnd = new Random();
        return rnd.nextInt(n+1);
    }
}
