package connectfour.rules;

import connectfour.model.Board;
import connectfour.model.Disc;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FourInARowDetectorTest {
    private final WinDetector detector = new FourInARowDetector();

    @Test
    void detectsHorizontalWin() {
        Board board = new Board();
        for (int column = 0; column < 3; column++) {
            board.drop(column, Disc.RED);
        }
        int row = board.drop(3, Disc.RED);

        assertTrue(detector.isWinningMove(board, row, 3));
    }

    @Test
    void detectsVerticalWin() {
        Board board = new Board();
        int row = 0;
        for (int i = 0; i < 4; i++) {
            row = board.drop(5, Disc.YELLOW);
        }

        assertTrue(detector.isWinningMove(board, row, 5));
    }

    @Test
    void detectsRisingDiagonalWin() {
        // red on (0,0), (1,1), (2,2), (3,3)
        Board board = new Board();
        board.drop(0, Disc.RED);
        board.drop(1, Disc.YELLOW);
        board.drop(1, Disc.RED);
        board.drop(2, Disc.YELLOW);
        board.drop(2, Disc.YELLOW);
        board.drop(2, Disc.RED);
        board.drop(3, Disc.YELLOW);
        board.drop(3, Disc.YELLOW);
        board.drop(3, Disc.YELLOW);
        int row = board.drop(3, Disc.RED);

        assertTrue(detector.isWinningMove(board, row, 3));
    }

    @Test
    void detectsFallingDiagonalWinWhenTheMiddleDiscIsPlayedLast() {
        // red on (3,0), (2,1), (1,2), (0,3), winning disc goes in the middle
        Board board = new Board();
        board.drop(0, Disc.YELLOW);
        board.drop(0, Disc.YELLOW);
        board.drop(0, Disc.YELLOW);
        board.drop(0, Disc.RED);
        board.drop(1, Disc.YELLOW);
        board.drop(1, Disc.YELLOW);
        board.drop(1, Disc.RED);
        board.drop(3, Disc.RED);
        board.drop(2, Disc.YELLOW);
        int row = board.drop(2, Disc.RED);

        assertTrue(detector.isWinningMove(board, row, 2));
    }

    @Test
    void threeInARowOrABrokenLineIsNotAWin() {
        Board board = new Board();
        board.drop(0, Disc.RED);
        board.drop(1, Disc.RED);
        board.drop(2, Disc.YELLOW);
        int row = board.drop(3, Disc.RED);
        board.drop(4, Disc.RED);

        assertFalse(detector.isWinningMove(board, row, 3));
        assertFalse(detector.isWinningMove(board, 0, 1));
    }
}
