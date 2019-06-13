package dnd.logic.random_generator;

import java.util.Random;

/**
 * Uses the random classes to produce random numbers
 */
public class Randomizer implements RandomGenerator {
    @Override
    public int nextInt(int n) {
        Random rnd = new Random();
        return rnd.nextInt(n + 1);
    }
}
