package connectfour.ui.console;

import connectfour.model.Board;
import connectfour.model.Disc;
import connectfour.model.Game;
import connectfour.model.GameListener;
import connectfour.model.Move;

import java.io.PrintStream;

public class ConsoleView implements GameListener {
    private final PrintStream out;

    public ConsoleView(PrintStream out) {
        this.out = out;
    }

    @Override
    public void onMovePlayed(Game game, Move move) {
        out.println();
        out.println(game.getPlayer(move.disc()) + " played column " + (move.column() + 1));
        out.print(render(game.getBoard()));
    }

    @Override
    public void onMoveUndone(Game game, Move move) {
        out.println();
        out.println("Undid " + game.getPlayer(move.disc()) + "'s move in column " + (move.column() + 1));
        out.print(render(game.getBoard()));
    }

    @Override
    public void onGameOver(Game game) {
        out.println();
        out.println(describeResult(game));
    }

    public void showStart(Game game) {
        out.println();
        out.println(game.getPlayers().get(0) + " vs " + game.getPlayers().get(1));
        out.print(render(game.getBoard()));
    }

    public void showHistory(Game game) {
        if (game.getMoveHistory().isEmpty()) {
            out.println("No moves yet.");
            return;
        }
        int number = 1;
        for (Move move : game.getMoveHistory()) {
            out.println(number++ + ". " + game.getPlayer(move.disc()) + " -> column " + (move.column() + 1));
        }
    }

    static String describeResult(Game game) {
        return switch (game.getStatus()) {
            case WON -> game.getWinner().map(winner -> winner + " wins!").orElse("Game over");
            case DRAW -> "It's a draw: the board is full.";
            case ABANDONED -> "Game abandoned.";
            case IN_PROGRESS -> "Game in progress";
        };
    }

    public static String render(Board board) {
        StringBuilder text = new StringBuilder();
        text.append(' ');
        for (int column = 1; column <= board.getColumns(); column++) {
            text.append(' ').append(column);
        }
        text.append('\n');
        for (int row = board.getRows() - 1; row >= 0; row--) {
            text.append('|');
            for (int column = 0; column < board.getColumns(); column++) {
                Disc disc = board.getDisc(row, column);
                text.append(' ').append(disc == null ? '.' : disc.getSymbol());
            }
            text.append(" |\n");
        }
        text.append('+').append("-".repeat(board.getColumns() * 2 + 1)).append("+\n");
        return text.toString();
    }
}
