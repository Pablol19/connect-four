package connectfour.ai;

import connectfour.rules.WinDetector;

import java.util.Random;

public enum Difficulty {
    EASY("Easy") {
        @Override
        public MoveStrategy createStrategy(WinDetector winDetector, Random random) {
            return new RandomStrategy(random);
        }
    },
    MEDIUM("Medium") {
        @Override
        public MoveStrategy createStrategy(WinDetector winDetector, Random random) {
            return new GreedyStrategy(winDetector, random);
        }
    },
    HARD("Hard") {
        @Override
        public MoveStrategy createStrategy(WinDetector winDetector, Random random) {
            return new MinimaxStrategy(winDetector, 6);
        }
    };

    private final String label;

    Difficulty(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public abstract MoveStrategy createStrategy(WinDetector winDetector, Random random);
}
