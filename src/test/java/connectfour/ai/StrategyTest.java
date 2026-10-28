package connectfour.ai;

import connectfour.model.Board;
import connectfour.model.Disc;
import connectfour.rules.FourInARowDetector;
import connectfour.rules.WinDetector;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StrategyTest {
    private final WinDetector detector = new FourInARowDetector();

    private Board boardWhereRedThreatensColumn3() {
        Board board = new Board();
        board.drop(0, Disc.RED);
        board.drop(1, Disc.RED);
        board.drop(2, Disc.RED);
        board.drop(0, Disc.YELLOW);
        board.drop(1, Disc.YELLOW);
        return board;
    }

    @Test
    void randomStrategyOnlyChoosesAvailableColumns() {
        Board board = new Board();
        for (int i = 0; i < Board.DEFAULT_ROWS; i++) {
            board.drop(0, i % 2 == 0 ? Disc.RED : Disc.YELLOW);
        }
        MoveStrategy strategy = new RandomStrategy(new Random(42));

        for (int i = 0; i < 100; i++) {
            assertTrue(board.canDropIn(strategy.chooseColumn(board, Disc.YELLOW)));
        }
    }

    @Test
    void everyNonRandomStrategyTakesAnImmediateWin() {
        List<MoveStrategy> strategies = List.of(
                new GreedyStrategy(detector, new Random(1)),
                new MinimaxStrategy(detector, 4));

        for (MoveStrategy strategy : strategies) {
            assertEquals(3, strategy.chooseColumn(boardWhereRedThreatensColumn3(), Disc.RED),
                    strategy.getClass().getSimpleName() + " should win");
        }
    }

    @Test
    void everyNonRandomStrategyBlocksTheOpponent() {
        List<MoveStrategy> strategies = List.of(
                new GreedyStrategy(detector, new Random(1)),
                new MinimaxStrategy(detector, 4));

        for (MoveStrategy strategy : strategies) {
            assertEquals(3, strategy.chooseColumn(boardWhereRedThreatensColumn3(), Disc.YELLOW),
                    strategy.getClass().getSimpleName() + " should block");
        }
    }

    @Test
    void greedyStrategyPrefersTheCentreOnAnEmptyBoard() {
        MoveStrategy strategy = new GreedyStrategy(detector, new Random(7));

        assertEquals(3, strategy.chooseColumn(new Board(), Disc.RED));
    }

    @Test
    void minimaxEvaluationIsSymmetricForBothPlayers() {
        Board board = boardWhereRedThreatensColumn3();
        MinimaxStrategy strategy = new MinimaxStrategy(detector, 2);

        assertEquals(strategy.evaluate(board, Disc.RED), -strategy.evaluate(board, Disc.YELLOW));
        assertTrue(strategy.evaluate(board, Disc.RED) > 0);
    }

    @Test
    void eachDifficultyCreatesItsOwnStrategy() {
        Random random = new Random(3);

        assertInstanceOf(RandomStrategy.class, Difficulty.EASY.createStrategy(detector, random));
        assertInstanceOf(GreedyStrategy.class, Difficulty.MEDIUM.createStrategy(detector, random));
        assertInstanceOf(MinimaxStrategy.class, Difficulty.HARD.createStrategy(detector, random));
    }
}
