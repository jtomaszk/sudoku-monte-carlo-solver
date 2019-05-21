package sample;

class UnresolvableSudokuException extends Exception {
    private Point point;
    private SudokuFieldValue value;

    public UnresolvableSudokuException(Point point, SudokuFieldValue value) {
        super(point + " value=" + value);
        this.point = point;
        this.value = value;
    }
}
