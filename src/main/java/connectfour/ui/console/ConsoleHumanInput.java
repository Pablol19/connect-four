package connectfour.ui.console;

import connectfour.model.Game;
import connectfour.player.HumanInput;
import connectfour.player.Player;

import java.io.PrintStream;
import java.util.Scanner;

public class ConsoleHumanInput implements HumanInput {
    private final Scanner in;
    private final PrintStream out;
    private final ConsoleView view;

    public ConsoleHumanInput(Scanner in, PrintStream out, ConsoleView view) {
        this.in = in;
        this.out = out;
        this.view = view;
    }

    @Override
    public void handleTurn(Game game, Player player) {
        int columns = game.getBoard().getColumns();
        while (true) {
            out.print(player + ", choose a column 1-" + columns + " (u = undo, h = history, q = quit): ");
            if (!in.hasNextLine()) {
                game.abandon();
                return;
            }
            String answer = in.nextLine().trim().toLowerCase();
            if (answer.equals("q")) {
                game.abandon();
                return;
            }
            if (answer.equals("h")) {
                view.showHistory(game);
                continue;
            }
            if (answer.equals("u")) {
                if (game.undoTurnOf(player) > 0) {
                    return;
                }
                out.println("There is nothing to undo.");
                continue;
            }
            try {
                game.playColumn(Integer.parseInt(answer) - 1);
                return;
            } catch (NumberFormatException e) {
                out.println("Please type a column number, u, h or q.");
            } catch (IllegalArgumentException e) {
                out.println(e.getMessage() + ". Try another one.");
            }
        }
    }
}
