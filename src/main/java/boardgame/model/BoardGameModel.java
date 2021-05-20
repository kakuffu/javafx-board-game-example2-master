package boardgame.model;

import javafx.beans.property.*;

public class BoardGameModel {

    public static int BOARD_ROW_SIZE = 1;
    public static int BOARD_COL_SIZE = 10;

    public enum Player {
        PLAYER1,
        PLAYER2;

        public Player next() {
            return switch (this) {
                case PLAYER1 -> PLAYER2;
                case PLAYER2 -> PLAYER1;
            };
        }
    }

    private  ReadOnlyObjectWrapper<Player> currentPlayer = new ReadOnlyObjectWrapper<>();

    private ReadOnlyObjectWrapper<Square>[][] board = new ReadOnlyObjectWrapper[BOARD_ROW_SIZE][BOARD_COL_SIZE];

    private ReadOnlyIntegerWrapper numberOfTails = new ReadOnlyIntegerWrapper();

    private ReadOnlyBooleanWrapper gameOver = new ReadOnlyBooleanWrapper();

    public BoardGameModel() {
        for (int i = 0; i < BOARD_ROW_SIZE; i++) {
            for (int j = 0; j < BOARD_COL_SIZE; j++) {
                board[i][j] = new ReadOnlyObjectWrapper<Square>(Square.HEAD);
            }
        }
        currentPlayer.set(Player.PLAYER1);
        numberOfTails.set(0);
        gameOver.bind(numberOfTails.isEqualTo(BOARD_ROW_SIZE*BOARD_COL_SIZE));
    }

    public  Player getCurrentPlayer() {
        return currentPlayer.get();
    }

    public ReadOnlyObjectProperty<Player> currentPlayerProperty() {
        return currentPlayer.getReadOnlyProperty();
    }

    public ReadOnlyObjectProperty<Square> squareProperty(int i, int j) {
        return board[i][j].getReadOnlyProperty();
    }

    public Square getSquare(int i, int j) {
        return board[i][j].get();
    }

    public int getNumberOfTails() {
        return numberOfTails.get();
    }

    public ReadOnlyIntegerProperty numberOfTailsProperty() {
        return numberOfTails.getReadOnlyProperty();
    }

    public boolean isGameOver() {
        return gameOver.get();
    }

    public ReadOnlyBooleanProperty gameOverProperty() {
        return gameOver.getReadOnlyProperty();
    }

    public void move(int i, int j) {
        int change =0;
        board[i][j].set(
                switch (board[i][j].get()) {
                    case HEAD -> {
                        change = 1;
                        yield Square.TAIL;
                    }
                    case TAIL -> {
                        change= -1;
                        yield Square.HEAD;
                    }
                }
        );
        if (change !=0) {
            numberOfTails.set(numberOfTails.get() + change);
        }
        currentPlayer.set(currentPlayer.get().next());

    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < BOARD_ROW_SIZE; i++) {
            for (int j = 0; j < BOARD_COL_SIZE; j++) {
                sb.append(board[i][j].get().ordinal()).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        var model = new BoardGameModel();
        System.out.println(model);
    }

}
