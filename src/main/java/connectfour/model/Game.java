package connectfour.model;

import connectfour.command.CommandHistory;
import connectfour.command.DropDiscCommand;
import connectfour.player.Player;
import connectfour.rules.WinDetector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Game {
    private final Board board;
    private final List<Player> players;
    private final WinDetector winDetector;
    private final CommandHistory<DropDiscCommand> history = new CommandHistory<>();
    private final List<GameListener> listeners = new ArrayList<>();

    private int currentPlayerIndex = 0;
    private GameStatus status = GameStatus.IN_PROGRESS;
    private Player winner;

    public Game(Board board, Player firstPlayer, Player secondPlayer, WinDetector winDetector) {
        this.board = Objects.requireNonNull(board, "board");
        this.winDetector = Objects.requireNonNull(winDetector, "winDetector");
        Objects.requireNonNull(firstPlayer, "firstPlayer");
        Objects.requireNonNull(secondPlayer, "secondPlayer");
        if (firstPlayer.getDisc() == secondPlayer.getDisc()) {
            throw new IllegalArgumentException("Both players cannot use the same disc");
        }
        this.players = List.of(firstPlayer, secondPlayer);
    }

    public void play() {
        while (!status.isOver()) {
            getCurrentPlayer().takeTurn(this);
        }
    }

    public void playColumn(int column) {
        if (status.isOver()) {
            throw new IllegalStateException("The game is already over");
        }
        if (!board.canDropIn(column)) {
            throw new IllegalArgumentException("Column " + (column + 1) + " is not available");
        }
        Player mover = getCurrentPlayer();
        DropDiscCommand command = new DropDiscCommand(board, column, mover.getDisc());
        command.execute();
        history.push(command);

        Move move = command.getMove();
        if (winDetector.isWinningMove(board, move.row(), move.column())) {
            status = GameStatus.WON;
            winner = mover;
        } else if (board.isFull()) {
            status = GameStatus.DRAW;
        } else {
            currentPlayerIndex = 1 - currentPlayerIndex;
        }

        listeners.forEach(listener -> listener.onMovePlayed(this, move));
        if (status.isOver()) {
            listeners.forEach(listener -> listener.onGameOver(this));
        }
    }

    public boolean undoLastMove() {
        if (history.isEmpty() || status == GameStatus.ABANDONED) {
            return false;
        }
        DropDiscCommand command = history.pop();
        Move move = command.getMove();
        command.undo();
        currentPlayerIndex = indexOf(move.disc());
        status = GameStatus.IN_PROGRESS;
        winner = null;
        listeners.forEach(listener -> listener.onMoveUndone(this, move));
        return true;
    }

    public int undoTurnOf(Player player) {
        int undone = 0;
        do {
            if (!undoLastMove()) {
                break;
            }
            undone++;
        } while (!history.isEmpty() && getCurrentPlayer() != player);
        return undone;
    }

    public void abandon() {
        if (status.isOver()) {
            return;
        }
        status = GameStatus.ABANDONED;
        listeners.forEach(listener -> listener.onGameOver(this));
    }

    public void addListener(GameListener listener) {
        listeners.add(Objects.requireNonNull(listener, "listener"));
    }

    public Board getBoard() {
        return board.copy();
    }

    public List<Move> getMoveHistory() {
        return history.inOrder().stream().map(DropDiscCommand::getMove).toList();
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getPlayer(Disc disc) {
        return players.get(indexOf(disc));
    }

    public GameStatus getStatus() {
        return status;
    }

    public Optional<Player> getWinner() {
        return Optional.ofNullable(winner);
    }

    private int indexOf(Disc disc) {
        return players.get(0).getDisc() == disc ? 0 : 1;
    }
}
