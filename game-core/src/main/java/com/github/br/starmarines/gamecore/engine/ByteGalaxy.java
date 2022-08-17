package com.github.br.starmarines.gamecore.engine;

import java.util.BitSet;

public class ByteGalaxy {

    /**
     * название игровой карты
     */
    private final String title;
    /**
     * матрица смежности графа планет. Используется для проверки есть ли связь между планетами (ребро в графе)
     * Имеет оптимизированный размер = planets.size * (planets.size - 1)/2   поскольку:
     *    1) ребра всегда двунаправлены, значит можно хранить в 2 раза меньше
     *    2) нету рефлексивных вершин в графе. Значит можно убрать диагональ единичной матрицы
     */
    private final BitSet planetMatrix;

    /**
     * 5 + 9 * N байт, где N - число планет
     * <br/>
     * 8 бит (1 байт) - состояние и результат игры
     * 4 бита - состояние игры (GameStateEnum)
     * 4 бита - результат игры (GameResultEnum)
     * 32 бита (4 байта) - номер шага игры
     * список вершин графа планет.
     * 72 бита (9 байт) для ускорения массив сортирован и сделан так, что "id планеты" = "id элемента в массиве"
     * 16 бит  (2 байта) идентификатор планеты
     * 16 бит  (2 байта) игрок-хозяин планеты
     * 32 бита (4 байта) число юнитов на планете
     * 8 бит   (1 байт) под тип планеты. Тип определяет прирост юнитов
     */
    private final byte[] planets;

    /**
     * максимальное число состояний/шагов в игре
     */
    private final int maxStepsCount;

    /**
     * число планет (вершин в графе)
     */
    private final int size;

    public ByteGalaxy(String title, byte[] planets, BitSet planetMatrix, int maxStepsCount, int size) {
        this.title = title;
        this.planets = planets;
        this.planetMatrix = planetMatrix;
        this.maxStepsCount = maxStepsCount;
        this.size = size;
    }

    public String getTitle() {
        return title;
    }

    public byte[] getPlanets() {
        return planets;
    }

    public BitSet getPlanetMatrix() {
        return planetMatrix;
    }

    public int getMaxStepsCount() {
        return maxStepsCount;
    }

    public int getSize() {
        return size;
    }

}
