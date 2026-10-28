package connectfour.model;

public interface GameListener {
    default void onMovePlayed(Game game, Move move) {
    }

    default void onMoveUndone(Game game, Move move) {
    }

    default void onGameOver(Game game) {
    }
}
