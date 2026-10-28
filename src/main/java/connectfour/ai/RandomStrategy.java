package connectfour.ai;

import connectfour.model.Board;
import connectfour.model.Disc;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class RandomStrategy implements MoveStrategy {
    private final Random random;

    public RandomStrategy(Random random) {
        this.random = Objects.requireNonNull(random, "random");
    }

    @Override
    public int chooseColumn(Board board, Disc me) {
        List<Integer> available = board.availableColumns();
        if (available.isEmpty()) {
            throw new IllegalStateException("The board is full");
        }
        return available.get(random.nextInt(available.size()));
    }
}
