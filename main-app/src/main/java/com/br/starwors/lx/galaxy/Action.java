package com.br.starwors.lx.galaxy;

import com.github.br.starmarines.game.api.galaxy.Move;


public class Action {

    private short from;
    private short to;
    private int amount;

    public Action() {
    }

    public Action(Move move) {
        this.from = move.getFrom().getId();
        this.to = move.getTo().getId();
        this.amount = move.getAmount();
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public short getFrom() {
        return from;
    }

    public void setFrom(short from) {
        this.from = from;
    }

    public short getTo() {
        return to;
    }

    public void setTo(short to) {
        this.to = to;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<action>");
        sb.append("<from>").append(from).append("</from>");
        sb.append("<to>").append(to).append("</to>");
        sb.append("<unitsCount>").append(amount).append("</unitsCount>");
        sb.append("</action>");

        return sb.toString();
    }

}
