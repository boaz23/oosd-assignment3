package dnd.logic;

public class Point {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public static double distance(Point p, Point q) {
        if (p == null) {
            throw new IllegalArgumentException("p is null.");
        }
        if (q == null) {
            throw new IllegalArgumentException("q is null.");
        }

        int dx = p.getX() - q.getX();
        int dy = p.getY() - q.getY();
        return Math.sqrt((dx * dx) + (dy * dy));
    }
}
