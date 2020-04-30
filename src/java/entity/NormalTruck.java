package entity;

public class NormalTruck extends Truck {
    public static double pricePerKm = 8.00;
    static int servicePeriod = 12; //months
    static int maxWeight = 2000; // kg
    public NormalTruck(String registrationNumber, int manufacturingYear) {
        super(registrationNumber, manufacturingYear);
    }
    public NormalTruck() {
        super();
    }
}
