package com.github.br.starmarines.gamecore.engine.utils;

import com.github.br.starmarines.gamecore.api.GameResultEnum;
import com.github.br.starmarines.gamecore.api.GameStateEnum;

public final class GameStateUtils {

    private static final byte GAME_CURRENT_STATE_MASK = 0x0F;
    private static final int GAME_RESULT_MASK = 0xF0;

    public static byte createState(GameStateEnum state, GameResultEnum result) {
        return (byte) ((state.getCode() << 4) | result.getCode());
    }

    public static GameStateEnum getGameState(byte[] planets) {
        byte stateByte = planets[0];
        return GameStateEnum.toEnum((byte) (stateByte >> 4));
    }

    public static void setGameState(GameStateEnum state, byte[] planets) {
        byte currentState = planets[0];
        planets[0] = (byte) ((GAME_CURRENT_STATE_MASK & currentState) | (state.getCode() << 4));
    }

    public static GameResultEnum getGameResult(byte[] planets) {
        byte currentState = planets[0];
        return GameResultEnum.toEnum((byte)((currentState << 4) >> 4));
    }

    public static void setGameResult(GameResultEnum result, byte[] planets) {
        byte currentState = planets[0];
        planets[0] = (byte) ((currentState & GAME_RESULT_MASK) | result.getCode());
    }

}
