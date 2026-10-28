package connectfour.rules;

import connectfour.model.Board;
import connectfour.model.Disc;

public class FourInARowDetector implements WinDetector {
    private static final int[][] DIRECTIONS = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};

    private final int lengthToWin;

    public FourInARowDetector() {
        this(4);
    }

    public FourInARowDetector(int lengthToWin) {
        if (lengthToWin < 2) {
            throw new IllegalArgumentException("lengthToWin must be at least 2");
        }
        this.lengthToWin = lengthToWin;
    }

    @Override
    public boolean isWinningMove(Board board, int row, int column) {
        Disc disc = board.getDisc(row, column);
        if (disc == null) {
            return false;
        }
        for (int[] direction : DIRECTIONS) {
            int inLine = 1
                    + countInDirection(board, row, column, direction[0], direction[1], disc)
                    + countInDirection(board, row, column, -direction[0], -direction[1], disc);
            if (inLine >= lengthToWin) {
                return true;
            }
        }
        return false;
    }

    private int countInDirection(Board board, int row, int column, int rowStep, int columnStep, Disc disc) {
        int count = 0;
        int r = row + rowStep;
        int c = column + columnStep;
        while (board.isInside(r, c) && board.getDisc(r, c) == disc) {
            count++;
            r += rowStep;
            c += columnStep;
        }
        return count;
    }
}
