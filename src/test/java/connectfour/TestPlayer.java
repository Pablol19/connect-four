package connectfour;

import connectfour.model.Disc;
import connectfour.model.Game;
import connectfour.player.AbstractPlayer;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class TestPlayer extends AbstractPlayer {
    private final Deque<Integer> script;

    public TestPlayer(String name, Disc disc, Integer... columns) {
        super(name, disc);
        this.script = new ArrayDeque<>(Arrays.asList(columns));
    }

    @Override
    public void takeTurn(Game game) {
        if (script.isEmpty()) {
            game.abandon();
        } else {
            game.playColumn(script.poll());
        }
    }
}
