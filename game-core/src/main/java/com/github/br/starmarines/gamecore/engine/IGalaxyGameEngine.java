package com.github.br.starmarines.gamecore.engine;

import com.github.br.starmarines.gamecore.api.StepResultEnum;
import com.github.br.starmarines.gamecore.api.GalaxyCreateGameDelta;

public interface IGalaxyGameEngine {

    StepResultEnum addPlayerMoves(short playerId, long[] moves);

    byte[] computeNextState();

    byte[] getCurrentGameState();

    String getTitle();

    void reset(GalaxyCreateGameDelta delta);

}
