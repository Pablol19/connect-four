package connectfour.player;

import connectfour.model.Game;

public interface HumanInput {
    void handleTurn(Game game, Player player);
}
