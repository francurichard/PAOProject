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


    public String getRequestDate() {
        return requestDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }
}
