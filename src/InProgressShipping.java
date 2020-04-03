public class InProgressShipping extends Shipping{
    private String trucksRegistrationNumber;
    private double unexpectedCosts;

    public InProgressShipping(Cargo cargo, double estimatedCosts, double shippingBid, String trucksRegistrationNumber) {
        super(cargo, estimatedCosts, shippingBid);
        this.trucksRegistrationNumber = trucksRegistrationNumber;
        this.unexpectedCosts = 0;
    }

    public String getTrucksRegistrationNumber() {
        return trucksRegistrationNumber;
    }

    public double getUnexpectedCosts() {
        return unexpectedCosts;
    }

    public void addCost(double cost) {
        this.unexpectedCosts += cost;
    }
}
