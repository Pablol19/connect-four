package connectfour.player;

import connectfour.model.Disc;

import java.util.Objects;

public abstract class AbstractPlayer implements Player {
    private final String name;
    private final Disc disc;

    protected AbstractPlayer(String name, Disc disc) {
        this.name = Objects.requireNonNull(name, "name");
        this.disc = Objects.requireNonNull(disc, "disc");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Disc getDisc() {
        return disc;
    }

    @Override
    public String toString() {
        return name + " (" + disc.getSymbol() + ")";
    }
}
