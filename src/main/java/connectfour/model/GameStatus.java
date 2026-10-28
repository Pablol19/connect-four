package connectfour.model;

public enum GameStatus {
    IN_PROGRESS(false),
    WON(true),
    DRAW(true),
    ABANDONED(true);

    private final boolean over;

    GameStatus(boolean over) {
        this.over = over;
    }

    public boolean isOver() {
        return over;
    }
}
