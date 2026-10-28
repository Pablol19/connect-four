package connectfour.player;

import connectfour.ai.MoveStrategy;
import connectfour.model.Disc;
import connectfour.model.Game;

import java.util.Objects;

public class ComputerPlayer extends AbstractPlayer {
    private final MoveStrategy strategy;

    public ComputerPlayer(String name, Disc disc, MoveStrategy strategy) {
        super(name, disc);
        this.strategy = Objects.requireNonNull(strategy, "strategy");
    }

    @Override
    public void takeTurn(Game game) {
        int column = strategy.chooseColumn(game.getBoard(), getDisc());
        game.playColumn(column);
    }
}
