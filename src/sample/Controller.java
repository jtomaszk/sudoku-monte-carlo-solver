package sample;

import java.util.ArrayList;
import java.util.List;

public class Controller {

    private SudokuSolver sudokuSolver = new SudokuSolver();

    public List<Integer> getBoard() {
        sudokuSolver.solve(1000_000);
        SudokuField[][] field = sudokuSolver.getCurrentField();


        List<Integer> ret = new ArrayList<>();
        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field[y].length; x++) {
                SudokuField sudokuField = field[x][y];
                ret.add(sudokuField.isSolved() ? sudokuField.getValue().ordinal() + 1 : 0);
            }
        }
        return ret;
    }
}

