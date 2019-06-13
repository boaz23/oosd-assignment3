package dnd.logic;

/**
 * The game turns object
 */
public class Tick {
    private final int value;

    public static final Tick Zero = new Tick();

    private Tick() {
        value = 0;
    }

    public Tick(int value) {
        if (value < Zero.value) {
            throw new IllegalArgumentException("tick value must be a non-negative number.");
        }

        this.value = value;
    }

    /*
    Returns a new tick with the value of 'value' if it is a non-negative number,
    otherwise return a tick representing zero.
     */
    public static Tick fromValueOrZero(int value) {
        return new Tick(Math.max(value, Zero.value));
    }

    public Tick clone() {
        return new Tick(value);
    }

    public int getValue() {
        return value;
    }

    public Tick decrement() {
        return fromValueOrZero(value - 1);
    }

    public Tick increment() {
        return new Tick(value + 1);
    }

    public boolean isGreaterThan(Tick other) {
        if (other == null) {
            throw new IllegalArgumentException("other tick is null.");
        }

        return value > other.value;
    }

    public boolean isGreaterOrEqualTo(Tick other) {
        if (other == null) {
            throw new IllegalArgumentException("other tick is null.");
        }

        return value >= other.value;
    }

    public boolean equals(Tick other) {
        if (other == null) {
            throw new IllegalArgumentException("other tick is null.");
        }

        return value == other.value;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Tick && equals((Tick)obj);

    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return value + "";
    }
}
