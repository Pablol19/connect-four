package connectfour;

import connectfour.ai.Difficulty;
import connectfour.model.Board;
import connectfour.model.Disc;
import connectfour.model.Game;
import connectfour.player.ComputerPlayer;
import connectfour.player.HumanInput;
import connectfour.player.HumanPlayer;
import connectfour.player.Player;
import connectfour.rules.FourInARowDetector;
import connectfour.rules.WinDetector;
import connectfour.ui.console.ConsoleHumanInput;
import connectfour.ui.console.ConsoleView;

import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        PrintStream out = System.out;
        WinDetector winDetector = new FourInARowDetector();
        ConsoleView view = new ConsoleView(out);
        HumanInput keyboard = new ConsoleHumanInput(in, out, view);

        out.println("=== Connect Four ===");
        out.println("1) Human vs Human");
        out.println("2) Human vs Computer");
        boolean againstComputer = ask(in, out, "Choose a mode [1-2]: ", "1", "2").equals("2");

        Player first = new HumanPlayer(askName(in, out, "Player 1 name: ", "Player 1"), Disc.RED, keyboard);
        Player second;
        if (againstComputer) {
            Difficulty difficulty = askDifficulty(in, out);
            second = new ComputerPlayer("Computer (" + difficulty.getLabel() + ")", Disc.YELLOW,
                    difficulty.createStrategy(winDetector, new Random()));
        } else {
            second = new HumanPlayer(askName(in, out, "Player 2 name: ", "Player 2"), Disc.YELLOW, keyboard);
        }

        Game game = new Game(new Board(), first, second, winDetector);
        game.addListener(view);
        view.showStart(game);
        game.play();
    }

    private static Difficulty askDifficulty(Scanner in, PrintStream out) {
        Difficulty[] levels = Difficulty.values();
        String[] options = new String[levels.length];
        for (int i = 0; i < levels.length; i++) {
            out.println((i + 1) + ") " + levels[i].getLabel());
            options[i] = String.valueOf(i + 1);
        }
        String choice = ask(in, out, "Choose a difficulty [1-" + levels.length + "]: ", options);
        return levels[Integer.parseInt(choice) - 1];
    }

    private static String askName(Scanner in, PrintStream out, String prompt, String fallback) {
        out.print(prompt);
        String name = in.hasNextLine() ? in.nextLine().trim() : "";
        return name.isEmpty() ? fallback : name;
    }

    private static String ask(Scanner in, PrintStream out, String prompt, String... valid) {
        while (true) {
            out.print(prompt);
            if (!in.hasNextLine()) {
                System.exit(0);
            }
            String answer = in.nextLine().trim();
            for (String option : valid) {
                if (option.equals(answer)) {
                    return answer;
                }
            }
            out.println("Please choose one of the listed options.");
        }
    }
}
