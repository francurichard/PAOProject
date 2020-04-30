package entity;

public class LargeTruck extends Truck{
    public static double pricePerKm = 10.00;
    static int servicePeriod = 9; // months

    public LargeTruck(String registrationNumber, int manufacturingYear) {
        super(registrationNumber, manufacturingYear);
    }

    public LargeTruck() {
        super();
    }
}
