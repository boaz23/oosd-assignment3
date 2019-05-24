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

    public int getY() {
        return y;
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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Point)) {
            return false;
        }

        Point other = (Point)obj;
        return this.x == other.x & this.y == other.y;
    }
}
