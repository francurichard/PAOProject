package entity;

import java.util.UUID;

public class Shipping {
    private String uuid;
    private Cargo cargo;
    private double estimatedCosts;
    private double shippingBid;

    public Shipping(Cargo cargo, double estimatedCosts, double shippingBid) {
        this.cargo = cargo;
        this.estimatedCosts = estimatedCosts;
        this.shippingBid = shippingBid;
        this.uuid = UUID.randomUUID().toString();
    }

    public Shipping(String uuid, Cargo cargo, double estimatedCosts, double shippingBid) {
        this.uuid = uuid;
        this.cargo = cargo;
        this.estimatedCosts = estimatedCosts;
        this.shippingBid = shippingBid;
    }

    public String getUuid() {
        return uuid;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public double getEstimatedCosts() {
        return estimatedCosts;
    }

    public double getShippingBid() {
        return shippingBid;
    }

    public double getShippingProfit() {
        return this.shippingBid - this.estimatedCosts;
    }

}
