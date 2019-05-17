package dnd.logic;

public class Tick {
    private int value;

    public Tick(int value) {
        this.value = value;
    }

//    public Tick clone() {
//        return new Tick(this.value);
//    }

    public int getValue() {
        return value;
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
