public class LargeTruck extends Truck{
    static double pricePerKm = 10.00;
    static int servicePeriod = 9; // months

    public LargeTruck(String registrationNumber, int manufacturingYear) {
        super(registrationNumber, manufacturingYear);
    }

    public LargeTruck() {
        super();
    }
}
