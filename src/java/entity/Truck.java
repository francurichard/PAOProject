package entity;

import java.util.Scanner;

public class Truck {
    private String registrationNumber;
    private int manufacturingYear;
    private boolean available;

    public Truck() {
        this.available = true;
    }

    public Truck(String registrationNumber, int manufacturingYear) {
        this.registrationNumber = registrationNumber;
        this.manufacturingYear = manufacturingYear;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public int getManufacturingYear() {
        return manufacturingYear;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public void setManufacturingYear(int manufacturingYear) {
        this.manufacturingYear = manufacturingYear;
    }

    @Override
    public String toString() {
        return String.format("Registration number " + this.registrationNumber + "\n" + "Manufacturing year: " + this.manufacturingYear);
    }

    public void readObject() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Registration number:");
        setRegistrationNumber(sc.nextLine());
        System.out.println("Manufacturing year:");
        setManufacturingYear(sc.nextInt());
        sc.nextLine();
    }

    public String toCsv() {
        return this.registrationNumber + "," + this.manufacturingYear;
    }
}
