package connectfour.player;

import connectfour.model.Disc;
import connectfour.model.Game;

import java.util.Objects;

public class HumanPlayer extends AbstractPlayer {
    private final HumanInput input;

    public HumanPlayer(String name, Disc disc, HumanInput input) {
        super(name, disc);
        this.input = Objects.requireNonNull(input, "input");
    }

    @Override
    public void takeTurn(Game game) {
        input.handleTurn(game, this);
    }
}
