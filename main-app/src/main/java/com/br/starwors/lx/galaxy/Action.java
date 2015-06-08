package com.br.starwors.lx.galaxy;

import com.br.game.api.galaxy.Move;


public class Action {


    private String from;
    private String to;
    private int amount;
    
    public Action(){    	
    }
    
    public Action(Move move){    	
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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<action>");
        sb.append(    "<from>");
        sb.append(         from);
        sb.append(    "</from>");
        sb.append(    "<to>");
        sb.append(         to);
        sb.append    ("</to>");
        sb.append(    "<unitsCount>");
        sb.append(         amount);
        sb.append(    "</unitsCount>");
        sb.append("</action>");

        return sb.toString();
    }


}
