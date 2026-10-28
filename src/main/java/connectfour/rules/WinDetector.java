package connectfour.rules;

import connectfour.model.Board;

public interface WinDetector {
    boolean isWinningMove(Board board, int row, int column);
}
