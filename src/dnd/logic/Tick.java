package dnd.logic;

public class Tick {
    private final int value;

    public static final Tick Zero = new Tick(0);

    public Tick(int value) {
        if (value < Zero.value) {
            throw new IllegalArgumentException("tick value must be a non-negative number.");
        }

        this.value = value;
    }

//    public Tick clone() {
//        return new Tick(this.value);
//    }

    public int getValue() {
        return value;
    }

    public Tick decrement() {
        return new Tick(Math.max(this.value - 1, Zero.value));
    }

    public boolean isGreaterThan(Tick other) {
        if (other == null) {
            throw new IllegalArgumentException("other tick is null.");
        }

        return this.value > other.value;
    }

    public boolean isGreaterOrEqualTo(Tick other) {
        if (other == null) {
            throw new IllegalArgumentException("other tick is null.");
        }

        return this.value >= other.value;
    }
}
