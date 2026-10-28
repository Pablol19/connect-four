package connectfour.model;

public enum Disc {
    RED('X'),
    YELLOW('O');

    private final char symbol;

    Disc(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    public Disc opponent() {
        return this == RED ? YELLOW : RED;
    }
}
