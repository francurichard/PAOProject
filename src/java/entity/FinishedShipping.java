package entity;

public class FinishedShipping extends Shipping {
    final private double finalProfit;
    final private double finalCosts;
    final private String date;

    public FinishedShipping(Cargo cargo, double estimatedCosts, double shippingBid, double unexpectedCosts, String date) {
        super(cargo, estimatedCosts, shippingBid);
        this.finalCosts = estimatedCosts + unexpectedCosts;
        this.finalProfit = shippingBid - this.finalCosts;
        this.date = date;
    }

    public FinishedShipping(Cargo cargo, double estimatedCosts, double shippingBid, double finalProfit, double finalCosts, String date) {
        super(cargo, estimatedCosts, shippingBid);
        this.finalProfit = finalProfit;
        this.finalCosts = finalCosts;
        this.date = date;
    }

    public FinishedShipping(String uuid, Cargo cargo, double estimatedCosts, double shippingBid, double finalProfit, double finalCosts, String date) {
        super(uuid, cargo, estimatedCosts, shippingBid);
        this.finalProfit = finalProfit;
        this.finalCosts = finalCosts;
        this.date = date;
    }

    public double getFinalProfit() {
        return finalProfit;
    }

    public String getDate() {
        return date;
    }

    public double getFinalCosts() {
        return finalCosts;
    }

    @Override
    public String toString() {
        return String.format("This shipping was finished on date " + getDate() + "with total costs of" + getFinalCosts() + "and final profit: " + getFinalProfit());
    }

    public String toCsv() {
        return "2," + this.getUuid() + "," + this.getCargo().toCsv() + "," + this.getEstimatedCosts() +
                "," + this.getShippingBid() + ",,,,," + this.finalCosts + "," + this.finalProfit + "," +
                this.date;
    }
}
