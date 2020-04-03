import java.util.Scanner;

public class ShippingRequest {
    Cargo cargo;
    double bid;

    public ShippingRequest(Cargo cargo, double bid) {
        this.cargo = cargo;
        this.bid = bid;
    }

    public ShippingRequest() {

    }
    public Cargo getCargo() {
        return cargo;
    }

    public double getBid() {
        return bid;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public void readObject() {
        Cargo cargo = new Cargo();
        cargo.readObject();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Your bid: ");
        double bid = scanner.nextDouble();
        scanner.nextLine();
        setBid(bid);
        setCargo(cargo);
    }

}
