package sample;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Random;

class SudokuField {
    private Point point;
    private SudokuFieldValue value;
    private EnumSet<SudokuFieldValue> availableValues;

    public SudokuField(Point point) {
        this.point = point;
        availableValues = EnumSet.allOf(SudokuFieldValue.class);
    }

    public SudokuField(SudokuField sudokuField) {
        this.point = sudokuField.point;
        this.value = sudokuField.value;
        this.availableValues = EnumSet.copyOf(sudokuField.availableValues);
    }

    public SudokuFieldValue getRandomAvailableValue(Random random) throws UnresolvableSudokuException {
        if (availableValues.size() == 0) {
            throw new UnresolvableSudokuException(point, value);
        }
        return availableValues.stream()
                .skip(random.nextInt(availableValues.size()))
                .findAny()
                .orElseThrow(RuntimeException::new);
    }

    public boolean removeAvailableValueSafe(SudokuFieldValue value) {
        availableValues.remove(value);
        return availableValues.size() > 1;
    }

    public void removeAvailableValue(SudokuFieldValue value) throws UnresolvableSudokuException {
        availableValues.remove(value);
        if (availableValues.size() == 0) {
            throw new UnresolvableSudokuException(point, value);
        }
    }

    public Point getPoint() {
        return point;
    }

    public SudokuFieldValue getValue() {
        return value;
    }

    public void setValue(SudokuFieldValue value) {
        this.value = value;
    }

    public EnumSet<SudokuFieldValue> getAvailableValues() {
        return availableValues;
    }

    public boolean isNotSolved() {
        return !isSolved();
    }

    public boolean isSolved() {
        return value != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SudokuField that = (SudokuField) o;
        return point.equals(that.point);
    }

    @Override
    public int hashCode() {
        return Objects.hash(point);
    }

    @Override
    public String toString() {
        return "SudokuField{" +
                "point=" + point +
                '}';
    }
}
