class ChangeCost {

    private double multipler;
    private double cost;
    private boolean isPercent;

    ChangeCost(double multipler, double cost, boolean isPercent) {
        this.multipler = multipler;
        this.cost = cost;
        this.isPercent = isPercent;
    }

    double getMultipler() {
        return multipler;
    }

    double getCost() {
        return cost;
    }

    boolean getIsPercent() {
        return isPercent;
    }
}
