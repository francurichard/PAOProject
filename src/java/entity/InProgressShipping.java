package entity;

public class InProgressShipping extends Shipping {
    private String trucksRegistrationNumber;
    private double unexpectedCosts;

    public InProgressShipping(Cargo cargo, double estimatedCosts, double shippingBid, String trucksRegistrationNumber) {
        super(cargo, estimatedCosts, shippingBid);
        this.trucksRegistrationNumber = trucksRegistrationNumber;
        this.unexpectedCosts = 0;
    }

    public InProgressShipping(Cargo cargo, double estimatedCosts, double shippingBid, String trucksRegistrationNumber, double unexpectedCosts) {
        super(cargo, estimatedCosts, shippingBid);
        this.trucksRegistrationNumber = trucksRegistrationNumber;
        this.unexpectedCosts = unexpectedCosts;
    }

    public InProgressShipping(String uuid, Cargo cargo, double estimatedCosts, double shippingBid, String trucksRegistrationNumber, double unexpectedCosts) {
        super(uuid, cargo, estimatedCosts, shippingBid);
        this.trucksRegistrationNumber = trucksRegistrationNumber;
        this.unexpectedCosts = unexpectedCosts;
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

    public String toCsv() {
        return "1," + this.getUuid() + "," + this.getCargo().toCsv() + "," + this.getEstimatedCosts() +
                "," + this.getShippingBid() + ",,," + this.trucksRegistrationNumber + "," + this.unexpectedCosts +
                ",,,";
    }
}
