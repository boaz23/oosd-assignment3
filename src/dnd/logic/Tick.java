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

    /*
    Returns a new tick with the value of 'value' if it is a non-negative number,
    otherwise return a tick representing zero.
     */
    public static Tick fromValueOrZero(int value) {
        return new Tick(Math.max(value, Zero.value));
    }

//    public Tick clone() {
//        return new Tick(this.value);
//    }

    public int getValue() {
        return value;
    }

    public Tick decrement() {
        return fromValueOrZero(this.value - 1);
    }
    public Tick increment() {
        return new Tick(this.value + 1);
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

    public boolean isEqual(Tick other) {
        if (other == null) {
            throw new IllegalArgumentException("other tick is null.");
        }

        return this.value == other.value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Tick)) {
            return false;
        }

        return this.equals((Tick)obj);
    }

    @Override
    public int hashCode() {
        return this.value;
    }
}
