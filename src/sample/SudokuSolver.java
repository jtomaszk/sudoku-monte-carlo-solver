package sample;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class SudokuSolver {
    private static final int FIELD_SIZE = 9;

    private Point[][] points = new Point[FIELD_SIZE][FIELD_SIZE];
    private Deque<Sudoku> backtrack = new ArrayDeque<>();
    private Sudoku currentField;
    private Random random = new Random();

    public SudokuSolver() {
        for (int x = 0; x < FIELD_SIZE; x++) {
            for (int y = 0; y < FIELD_SIZE; y++) {
                points[x][y] = new Point(x, y);
            }
        }

        IntStream.range(0, FIELD_SIZE * FIELD_SIZE)
                .mapToObj(value -> new Point(value % FIELD_SIZE, value / FIELD_SIZE))
                .forEach(point -> {
                    Point sudokuField = points[point.getX()][point.getY()];
                    sudokuField.setColumn(getColumn(points[point.getX()], sudokuField));
                    sudokuField.setRow(getRow(point, sudokuField));
                    sudokuField.setBox(getBox(point, sudokuField));
                });

        this.currentField = new Sudoku(points);
        saveState();
    }

    private Set<Point> getBox(Point point, Point sudokuField) {
        int xOffset = (point.getX() / 3) * 3;

        return IntStream.range(xOffset, xOffset + 3)
                .boxed()
                .flatMap(x -> {
                            int yOffset = (point.getY() / 3) * 3;
                            return IntStream.range(yOffset, yOffset + 3)
                                    .boxed()
                                    .map(y -> points[x][y]);
                        }
                ).filter(i -> !i.equals(sudokuField))
                .collect(Collectors.toSet());
    }

    private Set<Point> getRow(Point point, Point sudokuField) {
        return IntStream.range(0, FIELD_SIZE)
                .mapToObj(i -> points[i][point.getY()])
                .filter(i -> !i.equals(sudokuField))
                .collect(Collectors.toSet());
    }

    private Set<Point> getColumn(Point[] point, Point sudokuField) {
        return IntStream.range(0, FIELD_SIZE)
                .mapToObj(i -> point[i])
                .filter(i -> !i.equals(sudokuField))
                .collect(Collectors.toSet());
    }

    public void touchPoint(Point point) {
        while (testPoint(point)) {
//            System.out.println(point + " " + currentField.getForPoint(point).getAvailableValues());
        }
    }

    private Boolean testPoint(Point point) {
        return Optional.of(point)
                .map(i -> currentField.getForPoint(i))
                .filter(SudokuField::isNotSolved)
                .map(this::testField)
                .orElse(false);
    }

    private boolean testField(SudokuField i) {
        try {
            SudokuFieldValue testValue = i.getRandomAvailableValue(random);
//            System.out.println("test " + i + " value=" + testValue);
            if (currentField.isAllowedValue(i, testValue)) {
                if (i.removeAvailableValueSafe(testValue)) {
                    saveState();
                }
                currentField.setValue(i, testValue);
            } else {
                i.removeAvailableValue(testValue);
            }
            return false;
        } catch (UnresolvableSudokuException e) {
//            System.out.println("backtrack " + backtrack.size() + " " + e.getMessage());
            currentField = backtrack.removeLast();
            return true;
        }
    }

    private void saveState() {
        backtrack.addLast(new Sudoku(currentField));
    }

    public void solve(int iterations) {
        random.ints(iterations, 0, FIELD_SIZE * FIELD_SIZE)
                .mapToObj(value -> new Point(value % FIELD_SIZE, value / FIELD_SIZE))
                .forEach(this::touchPoint);
    }

    public void printCurrentResult() {
        currentField.print();
    }

    public SudokuField[][] getCurrentField() {
        return currentField.getField();
    }
}
