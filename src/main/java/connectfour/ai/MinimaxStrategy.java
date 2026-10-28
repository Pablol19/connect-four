package connectfour.ai;

import connectfour.model.Board;
import connectfour.model.Disc;
import connectfour.rules.WinDetector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class MinimaxStrategy implements MoveStrategy {
    private static final int WIN_SCORE = 100_000;
    private static final int INFINITY = 1_000_000;
    private static final int WINDOW = 4;

    private final WinDetector winDetector;
    private final int depth;

    public MinimaxStrategy(WinDetector winDetector, int depth) {
        if (depth < 1) {
            throw new IllegalArgumentException("depth must be at least 1");
        }
        this.winDetector = Objects.requireNonNull(winDetector, "winDetector");
        this.depth = depth;
    }

    @Override
    public int chooseColumn(Board board, Disc me) {
        List<Integer> columns = orderedColumns(board);
        if (columns.isEmpty()) {
            throw new IllegalStateException("The board is full");
        }
        int bestColumn = columns.get(0);
        int bestScore = -INFINITY;
        for (int column : columns) {
            int score = scoreMove(board, column, me, depth, -INFINITY, -bestScore);
            if (score > bestScore) {
                bestScore = score;
                bestColumn = column;
            }
        }
        return bestColumn;
    }

    private int scoreMove(Board board, int column, Disc disc, int remainingDepth, int alpha, int beta) {
        int row = board.drop(column, disc);
        int score;
        if (winDetector.isWinningMove(board, row, column)) {
            score = WIN_SCORE + remainingDepth; // prefer faster wins
        } else if (board.isFull()) {
            score = 0;
        } else {
            score = -negamax(board, remainingDepth - 1, alpha, beta, disc.opponent());
        }
        board.removeTop(column);
        return score;
    }

    private int negamax(Board board, int remainingDepth, int alpha, int beta, Disc toMove) {
        if (remainingDepth == 0) {
            return evaluate(board, toMove);
        }
        int best = -INFINITY;
        for (int column : orderedColumns(board)) {
            int score = scoreMove(board, column, toMove, remainingDepth, -beta, -alpha);
            best = Math.max(best, score);
            alpha = Math.max(alpha, score);
            if (alpha >= beta) {
                break;
            }
        }
        return best;
    }

    int evaluate(Board board, Disc perspective) {
        int score = 0;
        int centre = board.getColumns() / 2;
        for (int row = 0; row < board.getRows(); row++) {
            Disc disc = board.getDisc(row, centre);
            if (disc != null) {
                score += disc == perspective ? 3 : -3;
            }
        }
        int[][] directions = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};
        for (int row = 0; row < board.getRows(); row++) {
            for (int column = 0; column < board.getColumns(); column++) {
                for (int[] direction : directions) {
                    score += scoreWindow(board, row, column, direction[0], direction[1], perspective);
                }
            }
        }
        return score;
    }

    private int scoreWindow(Board board, int row, int column, int rowStep, int columnStep, Disc perspective) {
        int endRow = row + rowStep * (WINDOW - 1);
        int endColumn = column + columnStep * (WINDOW - 1);
        if (!board.isInside(endRow, endColumn)) {
            return 0;
        }
        int mine = 0;
        int theirs = 0;
        for (int i = 0; i < WINDOW; i++) {
            Disc disc = board.getDisc(row + rowStep * i, column + columnStep * i);
            if (disc == perspective) {
                mine++;
            } else if (disc != null) {
                theirs++;
            }
        }
        if (theirs == 0) {
            return lineValue(mine);
        }
        if (mine == 0) {
            return -lineValue(theirs);
        }
        return 0;
    }

    private int lineValue(int discs) {
        return discs == 3 ? 5 : discs == 2 ? 2 : 0;
    }

    private List<Integer> orderedColumns(Board board) {
        double centre = (board.getColumns() - 1) / 2.0;
        List<Integer> columns = new ArrayList<>(board.availableColumns());
        columns.sort(Comparator.comparingDouble(column -> Math.abs(column - centre)));
        return columns;
    }
}
