package sample;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

final class Point {
    private final int x;
    private final int y;

    private Set<Point> column;
    private Set<Point> row;
    private Set<Point> box;


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

    public void setColumn(Set<Point> column) {
        this.column = column;
    }

    public void setRow(Set<Point> row) {
        this.row = row;
    }

    public void setBox(Set<Point> box) {
        this.box = box;
    }

    public Stream<Point> getNeighbours() {
        return Stream.concat(Stream.concat(column.stream(), row.stream()), box.stream());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x &&
                y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
