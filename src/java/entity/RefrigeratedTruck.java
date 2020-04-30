package entity;

public class RefrigeratedTruck extends Truck {
    public static double pricePerKm = 12.00;
    static int servicePeriod = 6; // months

    public RefrigeratedTruck(String registrationNumber, int manufacturingYear) {
        super(registrationNumber, manufacturingYear);
    }

    public RefrigeratedTruck(){
        super();
    }
}
