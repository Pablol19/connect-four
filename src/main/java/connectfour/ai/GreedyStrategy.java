package connectfour.ai;

import connectfour.model.Board;
import connectfour.model.Disc;
import connectfour.rules.WinDetector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class GreedyStrategy implements MoveStrategy {
    private final WinDetector winDetector;
    private final Random random;

    public GreedyStrategy(WinDetector winDetector, Random random) {
        this.winDetector = Objects.requireNonNull(winDetector, "winDetector");
        this.random = Objects.requireNonNull(random, "random");
    }

    @Override
    public int chooseColumn(Board board, Disc me) {
        List<Integer> available = board.availableColumns();
        if (available.isEmpty()) {
            throw new IllegalStateException("The board is full");
        }

        Optional<Integer> win = findWinningColumn(board, me);
        if (win.isPresent()) {
            return win.get();
        }
        Optional<Integer> block = findWinningColumn(board, me.opponent());
        if (block.isPresent()) {
            return block.get();
        }

        List<Integer> safe = new ArrayList<>();
        for (int column : available) {
            if (!givesOpponentAWin(board, column, me)) {
                safe.add(column);
            }
        }
        List<Integer> candidates = safe.isEmpty() ? available : safe;
        return closestToCentre(candidates, board.getColumns());
    }

    private Optional<Integer> findWinningColumn(Board board, Disc disc) {
        for (int column : board.availableColumns()) {
            int row = board.drop(column, disc);
            boolean wins = winDetector.isWinningMove(board, row, column);
            board.removeTop(column);
            if (wins) {
                return Optional.of(column);
            }
        }
        return Optional.empty();
    }

    private boolean givesOpponentAWin(Board board, int column, Disc me) {
        board.drop(column, me);
        boolean opponentWins = findWinningColumn(board, me.opponent()).isPresent();
        board.removeTop(column);
        return opponentWins;
    }

    private int closestToCentre(List<Integer> columns, int totalColumns) {
        double centre = (totalColumns - 1) / 2.0;
        double bestDistance = columns.stream()
                .mapToDouble(column -> Math.abs(column - centre))
                .min()
                .orElseThrow();
        List<Integer> best = columns.stream()
                .filter(column -> Math.abs(column - centre) == bestDistance)
                .sorted(Comparator.naturalOrder())
                .toList();
        return best.get(random.nextInt(best.size()));
    }
}
