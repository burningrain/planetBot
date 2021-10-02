package com.github.br.starmarines.gamecore.api;

public class PlayerDto {

    private String color;
    private String name;
    private String strategy;

    public PlayerDto(String color, String name, String strategy) {
        this.color = color;
        this.name = name;
        this.strategy = strategy;
    }

    public PlayerDto(){}

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }
}
