package entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RequestedShipping extends Shipping {
    private String requestDate;
    private String expirationDate;
    public RequestedShipping(Cargo cargo, double estimatedCosts, double shippingBid) {
        super(cargo, estimatedCosts, shippingBid);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        this.requestDate = dtf.format(now);
        this.expirationDate = dtf.format(now.plusDays(7));
    }

    public RequestedShipping(Cargo cargo, double estimatedCosts, double shippingBid, String requestDate, String expirationDate) {
        super(cargo, estimatedCosts, shippingBid);
        this.requestDate = requestDate;
        this.expirationDate = expirationDate;
    }

    public RequestedShipping(String uuid, Cargo cargo, double estimatedCosts, double shippingBid, String requestDate, String expirationDate) {
        super(uuid, cargo, estimatedCosts, shippingBid);
        this.requestDate = requestDate;
        this.expirationDate = expirationDate;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String toCsv() {
        return "0," + this.getUuid() + "," + this.getCargo().toCsv() + "," + this.getEstimatedCosts() +
                "," + this.getShippingBid() + "," + this.requestDate + "," + this.expirationDate + ",,,,,";
    }
}
