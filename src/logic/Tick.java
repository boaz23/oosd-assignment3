package logic;

public class Tick {
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void decrement() {
        this.setValue(this.getValue() - 1);
    }
}
