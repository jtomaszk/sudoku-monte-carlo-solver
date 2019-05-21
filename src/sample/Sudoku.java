package sample;

import java.util.Set;
import java.util.stream.Collectors;

class Sudoku {
    public static final int FIELD_SIZE = 9;

    SudokuField[][] field;

    public Sudoku(Point[][] points) {
        SudokuField[][] initField = new SudokuField[FIELD_SIZE][FIELD_SIZE];

        for (int x = 0; x < FIELD_SIZE; x++) {
            for (int y = 0; y < FIELD_SIZE; y++) {
                initField[x][y] = new SudokuField(points[x][y]);
            }
        }
        this.field = initField;
    }

    public Sudoku(Sudoku sudoku) {
        this.field = new SudokuField[FIELD_SIZE][FIELD_SIZE];

        for (int x = 0; x < FIELD_SIZE; x++) {
            for (int y = 0; y < FIELD_SIZE; y++) {
                this.field[x][y] = new SudokuField(sudoku.field[x][y]);
            }
        }
    }

    public SudokuField getForPoint(Point point) {
        return field[point.getX()][point.getY()];
    }

    public SudokuField get(int x, int y) {
        return field[x][y];
    }

    public boolean isAllowedValue(SudokuField field, SudokuFieldValue value) {
        return !isNotAllowedValue(field, value);
    }

    public boolean isNotAllowedValue(SudokuField field, SudokuFieldValue value) {
        return field.getPoint().getNeighbours()
                .map(this::getForPoint)
                .filter(SudokuField::isSolved)
                .map(SudokuField::getValue)
                .anyMatch(i -> i == value);
    }

    public void setValue(SudokuField field, SudokuFieldValue value) throws UnresolvableSudokuException {
        field.setValue(value);

        Set<SudokuField> toVisit = field.getPoint().getNeighbours()
                .map(this::getForPoint)
                .filter(SudokuField::isNotSolved)
                .collect(Collectors.toSet());

        for (SudokuField i : toVisit) {
            i.removeAvailableValue(value);
        }
    }

    public void print() {
        for (int x = 0; x < FIELD_SIZE; x++) {
            for (int y = 0; y < FIELD_SIZE; y++) {
                System.out.print((field[x][y].getValue() != null ? field[x][y].getValue() : "x" + field[x][y].getAvailableValues().size()) + " ");
            }
            System.out.println();
        }
    }

    public SudokuField[][] getField() {
        return this.field;
    }
}
