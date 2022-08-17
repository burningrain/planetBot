package com.github.br.starmarines.gamecore.api;

import java.util.Objects;
import java.util.UUID;

public class GameInfo {

    private final UUID id;
    private final String title;

    private int gameId;
    private GameStateEnum state;

    public GameInfo(UUID id, String title) {
        this.id = id;
        this.title = Objects.requireNonNull(title);
        this.state = GameStateEnum.NOT_STARTED;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public GameStateEnum getState() {
        return state;
    }

    public void setState(GameStateEnum state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameInfo gameInfo = (GameInfo) o;
        return id.equals(gameInfo.id) && title.equals(gameInfo.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

}
