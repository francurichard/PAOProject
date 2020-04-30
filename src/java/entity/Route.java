package entity;

import java.util.Scanner;

public class Route {
    private String city1;
    private String city2;
    private double dist;

    public Route(String city1, String city2, double dist) {
        this.city1 = city1;
        this.city2 = city2;
        this.dist = dist;
    }

    public Route(String city1, String city2) {
        this.city1 = city1;
        this.city2 = city2;
    }

    public Route() {

    }

    public String getCity1() {
        return city1;
    }

    public String getCity2() {
        return city2;
    }

    public double getDist() {
        return dist;
    }

    public void setDist(double dist) {
        this.dist = dist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Route)) return false;
        Route route = (Route) o;
        return getCity1().equals(route.getCity1()) && getCity2().equals(route.getCity2()) ||
                getCity1().equals(route.getCity2()) && getCity2().equals(route.getCity1());
    }

    public void readObject(boolean readDist) {
        Scanner sc = new Scanner(System.in);
        System.out.println("First city");
        this.city1 = sc.nextLine();
        System.out.println("Second city");
        this.city2 = sc.nextLine();
        if (readDist) {
            System.out.println("Distance: (km)");
            this.dist = sc.nextDouble();
            sc.nextLine();
        }
    }

    public String toCsv() {
        return this.city1 + "," + this.city2 + "," + this.dist;
    }

}
