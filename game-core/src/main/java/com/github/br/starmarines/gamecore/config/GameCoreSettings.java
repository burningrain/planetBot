package com.github.br.starmarines.gamecore.config;

public class GameCoreSettings {

    private final int gameLoopExecutorsCount;
    private final int computingStepTimeInSec;
    private final int gamesMaxCapacity;

    private GameCoreSettings(Builder builder) {
        this.gameLoopExecutorsCount = builder.gameLoopExecutorsCount;
        this.computingStepTimeInSec = builder.computingStepTimeInSec;
        this.gamesMaxCapacity = builder.gamesMaxCapacity;
    }

    public int getGameLoopExecutorsCount() {
        return gameLoopExecutorsCount;
    }

    public int getComputingStepTimeInSec() {
        return computingStepTimeInSec;
    }

    public int getGamesMaxCapacity() {
        return gamesMaxCapacity;
    }

    public static class Builder {

        private int gameLoopExecutorsCount = 1;
        private int computingStepTimeInSec = 2;
        private int gamesMaxCapacity = 256;

        public Builder setGameLoopExecutorsCount(int gameLoopExecutorsCount) {
            this.gameLoopExecutorsCount = gameLoopExecutorsCount;
            return this;
        }

        public Builder setComputingStepTimeInSec(int computingStepTimeInSec) {
            this.computingStepTimeInSec = computingStepTimeInSec;
            return this;
        }

        public Builder setGamesMaxCapacity(int gamesMaxCapacity) {
            this.gamesMaxCapacity = gamesMaxCapacity;
            return this;
        }

        public GameCoreSettings build() {
            return new GameCoreSettings(this);
        }

    }


}
