package com.github.br.starmarines.coreplugins.internal.newgame;

import javafx.beans.property.*;
import javafx.scene.paint.Color;

public class PlayerBean {

    private IntegerProperty base = new SimpleIntegerProperty(this, "base");
    private SimpleObjectProperty<Color> color = new SimpleObjectProperty<>(this, "color");
    private StringProperty name = new SimpleStringProperty(this, "name");
    private StringProperty strategy = new SimpleStringProperty(this, "strategy");

    public PlayerBean(int base, Color color, String name, String strategy) {
        this.base.set(base);
        this.color.set(color);
        this.name.set(name);
        this.strategy.set(strategy);
    }

    public PlayerBean() {
    }

    public int getBase() {
        return base.get();
    }

    public IntegerProperty baseProperty() {
        return base;
    }

    public void setBase(int base) {
        this.base.set(base);
    }

    public Color getColor() {
        return color.get();
    }

    public ObjectProperty<Color> colorProperty() {
        return color;
    }

    public void setColor(Color color) {
        this.color.set(color);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getStrategy() {
        return strategy.get();
    }

    public StringProperty strategyProperty() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy.set(strategy);
    }

}