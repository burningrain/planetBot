package com.github.br.starmarines.gamecore.engine.utils;

import java.util.BitSet;

public final class PlanetMatrixUtils {

    private PlanetMatrixUtils() {
    }

    public static void createEdgeInMatrix(BitSet planetMatrix, int size, short planetId1, short planetId2) {
        planetMatrix.set(planetId1 * size + planetId2);
    }

    public static boolean checkEdgeInMatrix(BitSet planetMatrix, int size, short planetId1, short planetId2) {
        short min, max;
        if(planetId1 < planetId2) {
            min = planetId1;
            max = planetId2;
        } else {
            min = planetId2;
            max = planetId1;
        }

        int delta = 0;
        int zeroShift = min + 1;
        for(int i = 0; i < zeroShift; i++) {
            delta += i;
        }

        return planetMatrix.get(min * size + max - delta);
    }

}
