package com.github.br.starmarines.gamecore.api;

import java.util.List;

//todo утопить в ядро, переписать ядро
public class StartGameDto {

    private final String galaxy;
    private final List<PlayerDto> playerBeanList;

    public StartGameDto(String galaxy, List<PlayerDto> playerBeanList) {
        this.galaxy = galaxy;
        this.playerBeanList = playerBeanList;
    }

    public String getGalaxy() {
        return galaxy;
    }

    public List<PlayerDto> getPlayerDtoList() {
        return playerBeanList;
    }

}
