package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * The class <b>Main</b> is a Sudoku game that can be played using a GUI
 * provided by JavaFX.
 *
 * @author Benoît
 */
public class Main extends Application {

    private int value = 0;
    private long countUp = 0;

    private Scene scene;
    private GridPane table;
    private Controller controller;

    private List<Integer> board;
    private List<Integer> untouched;
    private Map<Integer, Button> boardText;
    private Map<Integer, Button> numButtons;
    private Map<Integer, GridPane> grid;

    private Button clear;
    private Button newGame;

    private Date start;
    private Timeline timeline;

    private Stage stage;

    /**
     * Main method for the Sudoku game
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Changes the CSS ids of the horizontal line
     *
     * @param array an Array of CSS ids
     * @param start the horizontal line number
     */
    private void changeHorizontalIds(String[] array, int start) {
        for (int i = start * 9; i < start * 9 + 9; i++) {
            changeIdsHelper(array, i);
        }
    }

    /**
     * Changes the CSS ids of the vertical line
     *
     * @param array an Array of CSS ids
     * @param start the vertical line number
     */
    private void changeVerticalIds(String[] array, int start) {
        for (int i = start; i < start + 9 * 9; i += 9) {
            changeIdsHelper(array, i);
        }
    }

    /**
     * Changes the CSS ids of a specific Sudoku board element according to its
     * original state
     *
     * @param array an Array of CSS ids
     * @param i     the location of the button in the Sudoku board
     */
    private void changeIdsHelper(String[] array, int i) {
        if (!(boardText.get(i).getText()).equals(String.valueOf(value)) || value == 0) {
            if (untouched.get(i) != 0) {
                boardText.get(i).setId(array[0]);
            } else if (board.get(i) != 0) {
                boardText.get(i).setId(array[1]);
            } else {
                boardText.get(i).setId(array[2]);
            }
        } else {
            boardText.get(i).setId(array[3]);
        }
    }

    /**
     * Resets the game
     */
    private void reset() {
        // Removes every buttons (GridPane) inside the main GridPane
        for (int i = 0; i < 9; i++) {
            table.getChildren().remove(grid.get(i));
        }

        // Creates a new Sudoku board for the player
        startGame();
    }

    /**
     * Returns the number of elements of the specified number
     *
     * @param num the number researched
     * @return a number of elements equal to the parameter
     */
    private int getNum(int num) {
        int count = 0;
        for (int p = 0; p < 81; p++) {
            if (Integer.valueOf(boardText.get(p).getText()) == num) {
                count++;
            }
        }
        return count;
    }

    /**
     * Generates the board, in terms of GUI
     */
    private void generateBoard() {
        // Each block
        for (int i = 0; i < 9; i++) {

            grid.put(i, new GridPane());

            int t = i % 3 * 3 + (i / 3) * 27;
            int temp = 0;

            // Each element in that block
            for (int j = t; j < t + 20; j += 9, temp++) {

                // Each row of the block
                for (int k = 0; k < 3; k++) {

                    // Index of current element
                    final int pos = j + k;

                    // New Button
                    boardText.put(pos, new Button());

                    if (board.get(pos) == 0) {
                        boardText.get(pos).setId("zero");

                        boardText.get(pos).setOnAction(e -> {
                            if (value != 0) {
                                if (boardText.get(pos).getText().equals(String.valueOf(value))) {
                                    boardText.get(pos).setText("0");
                                    board.set(pos, 0);
                                    boardText.get(pos).setId("helperZero");
                                } else {
                                    boardText.get(pos).setText(String.valueOf(value));
                                    boardText.get(pos).setId("");
                                    board.set(pos, value);
                                }

                                for (int l = 0; l < 81; l++) {
                                    if (!boardText.get(l).getId().equals("number")
                                            && boardText.get(l).getText().equals(String.valueOf(value))) {
                                        boardText.get(l).setId("number");
                                    }
                                }

                                setLegend();
                            }

                            // Checks if the game is done
//                            if (controller.checkBoard(board)) {
//                                timeline.stop();
//
//                                Alert alert = new Alert(AlertType.NONE,
//                                        "You just completed the controller board in " + countUp / 1000
//                                                + " seconds. Do you want to play again?",
//                                        ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
//                                alert.showAndWait();
//
//                                if (alert.getResult() == ButtonType.YES) {
//                                    newGame.fire();
//                                } else if (alert.getResult() == ButtonType.NO) {
//                                    stage.close();
//                                } else if (alert.getResult() == ButtonType.CANCEL) {
//                                    clear.fire();
//                                }
//                            }
                        });

                    } else {
                        boardText.get(pos).setId("preset");
                    }

                    boardText.get(pos).setOnMouseEntered(e -> {
                    });

                    boardText.get(pos).setOnMouseExited(e -> {
                    });

                    boardText.get(pos).setText(String.valueOf(board.get(pos)));
                    grid.get(i).add(boardText.get(pos), k, temp);
                }
            }
            table.add(grid.get(i), i % 3, i / 3);
        }
    }

    /**
     * Sets up the legend state by checking if any of the number has nine or more
     * appearance in the player's Sudoku board
     */
    private void setLegend() {
        for (int i = 1; i < 10; i++) {
            if (getNum(i) >= 9) {
                if (!"legendFull".equals(numButtons.get(i - 1).getId())) {
                    numButtons.get(i - 1).setId("legendFull");
                }
            } else if (i != value) {
                numButtons.get(i - 1).setId("");
            } else {
                numButtons.get(i - 1).setId("legend");
            }
        }
    }

    /**
     * Counts the time elapsed in seconds from the start of the game, from 0 to
     * infinite and display that number in the title bard
     */
    private void startTimer() {
        start = Calendar.getInstance().getTime();
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            countUp = Calendar.getInstance().getTime().getTime() - start.getTime();
            stage.setTitle(
                    "Sudoku - Time: " + TimeUnit.SECONDS.convert(countUp, TimeUnit.MILLISECONDS));
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    @Override
    public void start(Stage primaryStage) {
        // Creates a reference to the primaryStage to be
        // able to manipulate it in other methods
        stage = primaryStage;

        // Clear button
        clear = new Button("Clear");
        clear.setOnAction(e -> {
            board = Collections.nCopies(81, 0);
            for (int i = 0; i < 81; i++) {
                if (!Objects.equals(board.get(i), Integer.valueOf(boardText.get(i).getText()))) {
                    boardText.get(i).setText(String.valueOf(board.get(i)));
                    boardText.get(i).setId("zero");
                }
            }

            setLegend();
        });

        // New game button
        newGame = new Button("New Game");
        newGame.setOnAction(e -> {
            if (value != 0) {
                numButtons.get(value - 1).setId("");
                value = 0;
            }
            timeline.stop();
            stage.setTitle("Sudoku - Time: 0");
            reset();
            generateBoard();
            startTimer();
            setLegend();
        });

        // Starts the timer
        startTimer();

        // Layout of the board
        table = new GridPane();
        table.setVgap(8);
        table.setHgap(8);
        table.setAlignment(Pos.CENTER);

        // Layout of the nine numbers at the bottom (legend)
        GridPane num = new GridPane();
        num.setHgap(2);
        num.setPadding(new Insets(0, 0, 16, 0));
        num.setAlignment(Pos.CENTER);

        // Layout of the top two buttons
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setPadding(new Insets(16, 0, 0, 0));
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(newGame, clear);

        // Main layout of the Game
        BorderPane root = new BorderPane();
        root.setTop(hbox);
        root.setCenter(table);
        root.setBottom(num);

        startGame();
        numButtons = new HashMap<>();

        // Generates the GUI for the board
        generateBoard();

        // Sets up the legend (nine numbers at the bottom)
        for (int i = 0; i < 9; i++) {
            numButtons.put(i, new Button());
            numButtons.get(i).setText(String.valueOf(i + 1));
            num.add(numButtons.get(i), i, 0);

            final int lo = i + 1;

            numButtons.get(i).setOnAction(e -> {

                if (value == Integer.valueOf(numButtons.get(lo - 1).getText())) {
                    if (getNum(value) < 9) {
                        numButtons.get(value - 1).setId("");
                    }

                    for (int k = 0; k < 81; k++) {
                        if ((boardText.get(k).getText()).equals(String.valueOf(value))) {
                            if (untouched.get(k) != 0) {
                                boardText.get(k).setId("preset");
                            } else if (board.get(k) != 0) {
                                boardText.get(k).setId("");
                            }
                        }
                    }

                    value = 0;
                } else {
                    if (value != 0 && getNum(value) < 9) {
                        numButtons.get(value - 1).setId("");
                    }

                    value = lo;
                    numButtons.get(value - 1).setId("legend");

                    for (int k = 0; k < 81; k++) {
                        if ((boardText.get(k).getText()).equals(String.valueOf(value))) {
                            boardText.get(k).setId("number");
                        } else {
                            if (untouched.get(k) != 0) {
                                boardText.get(k).setId("preset");
                            } else if (board.get(k) != 0) {
                                boardText.get(k).setId("");
                            }
                        }
                    }
                }

                if (getNum(value) >= 9 && value != 0) {
                    numButtons.get(value - 1).setId("legendFull");
                }
            });

            numButtons.get(i).setOnMouseEntered(e -> scene.setCursor(Cursor.HAND));

            numButtons.get(i).setOnMouseExited(e -> scene.setCursor(Cursor.DEFAULT));
        }

        // Sets up the state of the legend (nine numbers at the bottom) according to the
        // player's Sudoku board
        setLegend();

        // Sets the scene to the BorderPane layout and links the CSS file
        scene = new Scene(root, 350, 450);
        scene.getStylesheets().add("file:resources/application.css");

        // Sets the stage, sets its title, displays it, and restricts its minimal size
        primaryStage.setScene(scene);
        primaryStage.setTitle("Sudoku - Time: 0");
        primaryStage.show();
        primaryStage.setMinHeight(primaryStage.getHeight());
        primaryStage.setMinWidth(primaryStage.getWidth());
    }

    private void startGame() {
        controller = new Controller();
        board = controller.getBoard();

        untouched = new ArrayList<>(board);
        boardText = new HashMap<>();
        grid = new HashMap<>();
    }
}
