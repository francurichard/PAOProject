public class FinishedShipping extends Shipping{
    private double finalProfit;
    private double finalCosts;
    private String date;

    public FinishedShipping(Cargo cargo, double estimatedCosts, double shippingBid, String date, double unexpectedCosts) {
        super(cargo, estimatedCosts, shippingBid);
        this.finalCosts = estimatedCosts + unexpectedCosts;
        this.finalProfit = shippingBid - finalCosts;
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
}
