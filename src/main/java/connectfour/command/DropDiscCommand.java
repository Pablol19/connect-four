package connectfour.command;

import connectfour.model.Board;
import connectfour.model.Disc;
import connectfour.model.Move;

public class DropDiscCommand implements Command {
    private final Board board;
    private final int column;
    private final Disc disc;
    private Move move;

    public DropDiscCommand(Board board, int column, Disc disc) {
        this.board = board;
        this.column = column;
        this.disc = disc;
    }

    @Override
    public void execute() {
        if (move != null) {
            throw new IllegalStateException("This move has already been played");
        }
        int row = board.drop(column, disc);
        move = new Move(column, row, disc);
    }

    @Override
    public void undo() {
        if (move == null) {
            throw new IllegalStateException("This move has not been played");
        }
        board.removeTop(column);
        move = null;
    }

    public Move getMove() {
        return move;
    }
}
