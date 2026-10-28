package connectfour.player;

import connectfour.model.Disc;
import connectfour.model.Game;

public interface Player {
    String getName();

    Disc getDisc();

    void takeTurn(Game game);
}
