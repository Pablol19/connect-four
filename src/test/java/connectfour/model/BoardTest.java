package connectfour.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardTest {
    @Test
    void discsStackFromTheBottomOfAColumn() {
        Board board = new Board();

        assertEquals(0, board.drop(3, Disc.RED));
        assertEquals(1, board.drop(3, Disc.YELLOW));
        assertEquals(Disc.RED, board.getDisc(0, 3));
        assertEquals(Disc.YELLOW, board.getDisc(1, 3));
        assertNull(board.getDisc(2, 3));
    }

    @Test
    void cannotDropIntoAFullColumn() {
        Board board = new Board();
        for (int i = 0; i < Board.DEFAULT_ROWS; i++) {
            board.drop(0, i % 2 == 0 ? Disc.RED : Disc.YELLOW);
        }

        assertTrue(board.isColumnFull(0));
        assertFalse(board.availableColumns().contains(0));
        assertThrows(IllegalArgumentException.class, () -> board.drop(0, Disc.RED));
    }

    @Test
    void cannotDropOutsideTheBoard() {
        Board board = new Board();

        assertThrows(IllegalArgumentException.class, () -> board.drop(-1, Disc.RED));
        assertThrows(IllegalArgumentException.class, () -> board.drop(7, Disc.RED));
    }

    @Test
    void removeTopUndoesTheLastDropInAColumn() {
        Board board = new Board();
        board.drop(2, Disc.RED);
        board.drop(2, Disc.YELLOW);

        assertEquals(Disc.YELLOW, board.removeTop(2));
        assertEquals(1, board.getHeight(2));
        assertNull(board.getDisc(1, 2));
    }

    @Test
    void copyIsIndependentOfTheOriginal() {
        Board board = new Board();
        board.drop(4, Disc.RED);

        Board copy = board.copy();
        copy.drop(4, Disc.YELLOW);

        assertEquals(1, board.getHeight(4));
        assertEquals(2, copy.getHeight(4));
    }
}
