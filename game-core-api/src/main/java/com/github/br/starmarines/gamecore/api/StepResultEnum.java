package com.github.br.starmarines.gamecore.api;

/**
 * ошибки имеют отрицательные значения по дизайну
 *
 */
public enum StepResultEnum {

    OK((byte) 0),

    EDGE_NOT_EXIST_ERROR((byte) -1),
    PLAYER_IS_NOT_OWNER_ERROR((byte) -2),
    UNITS_ARE_NOT_ENOUGH_ERROR((byte) -3),
    UNITS_AMOUNT_IS_NEGATIVE_OR_ZERO_ERROR((byte) -4),
    TOO_LATE_THE_GAME_IN_COMPUTING_STATE_ERROR((byte) -5)
    ;

    private final byte code;

    StepResultEnum(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

}
