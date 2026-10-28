package connectfour.ai;

import connectfour.model.Board;
import connectfour.model.Disc;

public interface MoveStrategy {
    int chooseColumn(Board board, Disc me);
}
