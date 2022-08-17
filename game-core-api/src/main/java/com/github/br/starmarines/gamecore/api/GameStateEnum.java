package com.github.br.starmarines.gamecore.api;

import java.util.Objects;

/**
 * НЕ БОЛЕЕ 16 СОСТОЯНИЙ!!!
 * пакуется в 1 байт вместе с GameResultEnum
 */
public enum GameStateEnum {

    NOT_STARTED((byte) 0),
    IN_PROCESS((byte) 1),
    FINISHED((byte) 2),
    PAUSED((byte) 3);

    private final byte code;

    private static final GameStateEnum[] states = new GameStateEnum[]{NOT_STARTED, IN_PROCESS, FINISHED, PAUSED};

    static {
        for (GameStateEnum value : GameStateEnum.values()) {
            states[value.getCode()] = value;
        }
    }

    public static GameStateEnum toEnum(byte b) {
        return Objects.requireNonNull(states[b], "state must not be null. index=[" + b + "]");
    }

    GameStateEnum(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

}
