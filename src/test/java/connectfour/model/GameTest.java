package connectfour.model;

import connectfour.TestPlayer;
import connectfour.player.Player;
import connectfour.rules.FourInARowDetector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameTest {
    private Player red;
    private Player yellow;
    private Game game;

    @BeforeEach
    void setUp() {
        red = new TestPlayer("Red", Disc.RED);
        yellow = new TestPlayer("Yellow", Disc.YELLOW);
        game = new Game(new Board(), red, yellow, new FourInARowDetector());
    }

    @Test
    void playersAlternateTurns() {
        assertSame(red, game.getCurrentPlayer());
        game.playColumn(3);
        assertSame(yellow, game.getCurrentPlayer());
        game.playColumn(3);
        assertSame(red, game.getCurrentPlayer());
    }

    @Test
    void completingFourInARowWinsAndEndsTheGame() {
        playColumns(0, 0, 1, 1, 2, 2, 3); // red plays the bottom row 0..3

        assertEquals(GameStatus.WON, game.getStatus());
        assertSame(red, game.getWinner().orElseThrow());
        assertThrows(IllegalStateException.class, () -> game.playColumn(4));
    }

    @Test
    void fullBoardWithoutAWinnerIsADraw() {
        // fills the board without anyone getting four in a row
        int[] order = {0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0,
                       2, 3, 2, 3, 2, 3, 3, 2, 3, 2, 3, 2,
                       4, 5, 4, 5, 4, 5, 5, 4, 5, 4, 5, 4,
                       6, 6, 6, 6, 6, 6};
        playColumns(order);

        assertEquals(GameStatus.DRAW, game.getStatus());
        assertTrue(game.getWinner().isEmpty());
    }

    @Test
    void undoRestoresTheBoardAndTheTurn() {
        game.playColumn(2);
        game.playColumn(4);

        assertTrue(game.undoLastMove());

        assertEquals(0, game.getBoard().getHeight(4));
        assertSame(yellow, game.getCurrentPlayer());
        assertEquals(1, game.getMoveHistory().size());
    }

    @Test
    void undoingAWinningMoveReopensTheGame() {
        playColumns(0, 0, 1, 1, 2, 2, 3);

        game.undoLastMove();

        assertEquals(GameStatus.IN_PROGRESS, game.getStatus());
        assertTrue(game.getWinner().isEmpty());
        assertSame(red, game.getCurrentPlayer());
    }

    @Test
    void undoTurnTakesBackTheOpponentReplyToo() {
        game.playColumn(3); // red
        game.playColumn(4); // yellow

        int undone = game.undoTurnOf(red);

        assertEquals(2, undone);
        assertTrue(game.getBoard().isEmpty());
        assertSame(red, game.getCurrentPlayer());
    }

    @Test
    void undoWithNoMovesDoesNothing() {
        assertFalse(game.undoLastMove());
        assertEquals(0, game.undoTurnOf(red));
    }

    @Test
    void listenersAreNotifiedOfMovesUndoAndGameOver() {
        List<String> events = new ArrayList<>();
        game.addListener(new GameListener() {
            @Override
            public void onMovePlayed(Game g, Move move) {
                events.add("played " + move.column());
            }

            @Override
            public void onMoveUndone(Game g, Move move) {
                events.add("undone " + move.column());
            }

            @Override
            public void onGameOver(Game g) {
                events.add("over " + g.getStatus());
            }
        });

        game.playColumn(5);
        game.undoLastMove();
        game.abandon();

        assertEquals(List.of("played 5", "undone 5", "over ABANDONED"), events);
    }

    @Test
    void playRunsTheGameUntilSomeoneWins() {
        Player scriptedRed = new TestPlayer("Red", Disc.RED, 6, 6, 6, 6);
        Player scriptedYellow = new TestPlayer("Yellow", Disc.YELLOW, 0, 1, 2);
        Game scripted = new Game(new Board(), scriptedRed, scriptedYellow, new FourInARowDetector());

        scripted.play();

        assertEquals(GameStatus.WON, scripted.getStatus());
        assertSame(scriptedRed, scripted.getWinner().orElseThrow());
        assertEquals(7, scripted.getMoveHistory().size());
    }

    @Test
    void playersMustUseDifferentDiscs() {
        Player otherRed = new TestPlayer("Other", Disc.RED);

        assertThrows(IllegalArgumentException.class,
                () -> new Game(new Board(), red, otherRed, new FourInARowDetector()));
    }

    private void playColumns(int... columns) {
        for (int column : columns) {
            game.playColumn(column);
        }
    }
}
