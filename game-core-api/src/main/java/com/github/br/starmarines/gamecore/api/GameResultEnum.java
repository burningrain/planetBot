package com.github.br.starmarines.gamecore.api;

import java.util.Objects;

/**
 * НЕ БОЛЕЕ 16 СОСТОЯНИЙ!!!
 * пакуется в 1 байт вместе с GameStateEnum
 */
public enum GameResultEnum {

    UNKNOWN((byte) 0),
    PLAYER_WIN((byte) 1),
    STEPS_AMOUNT_IS_OVER((byte) 2);

    private final byte code;

    private static final GameResultEnum[] states = new GameResultEnum[]{UNKNOWN, PLAYER_WIN, STEPS_AMOUNT_IS_OVER};

    GameResultEnum(byte code) {
        this.code = code;
    }

    public static GameResultEnum toEnum(byte result) {
        return Objects.requireNonNull(states[result], "state must not be null. index=[" + result + "]");
    }

    public byte getCode() {
        return code;
    }

}
