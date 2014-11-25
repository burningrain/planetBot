package starwors.model.lx.galaxy;


public class Action {


    private String from;
    private String to;
    private int amount;

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
            sb.append("<from>" + this.getFrom() + "</from>");
            sb.append("<to>" + this.getTo() + "</to>");
            sb.append("<unitsCount>" + this.getAmount() + "</unitsCount>");
        sb.append("</action>");

        return sb.toString();
    }


}
