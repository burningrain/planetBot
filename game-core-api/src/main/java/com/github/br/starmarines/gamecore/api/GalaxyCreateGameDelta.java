package com.github.br.starmarines.gamecore.api;

/**
 * Дельта изменений, накладываемая на конкретную карту
 */
public class GalaxyCreateGameDelta {

    private int playersCount;

    public int getPlayersCount() {
        return playersCount;
    }

    public void setPlayersCount(int playersCount) {
        this.playersCount = playersCount;
    }

}
