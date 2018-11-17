public class ChangeCost {

    private double multipler;
    private double cost;
    private boolean isPercent;

    public ChangeCost(double multipler, double cost, boolean isPercent) {
        this.multipler = multipler;
        this.cost = cost;
        this.isPercent = isPercent;
    }

    public double getMultipler() {
        return multipler;
    }

    public void setMultipler(int multipler) {
        this.multipler = multipler;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public boolean getIsPercent() {
        return isPercent;
    }
}
