import java.util.Scanner;

public class Cargo {
    private int weight;
    private boolean requireRefrigeratedTransport;
    private Route route;

    public Cargo(int weight, boolean requireRefrigeratedTransport, Route route) {
        this.weight = weight;
        this.requireRefrigeratedTransport = requireRefrigeratedTransport;
        this.route = route;
    }

    public Cargo(int weight, Route route) {
        this.weight = weight;
        this.route = route;
        this.requireRefrigeratedTransport = false;
    }

    public Cargo() {

    }

    public int getWeight() {
        return weight;
    }

    public boolean isRequireRefrigeratedTransport() {
        return requireRefrigeratedTransport;
    }

    public Route getRoute() {
        return route;
    }

    public int getNecessaryTruck() {
        if (isRequireRefrigeratedTransport()) {
            return 2;
        }
        if (getWeight() <= NormalTruck.maxWeight) {
            return 1;
        } else {
            return 3;
        }
    }

    public void readObject() {
        Route route = new Route();
        route.readObject(false);
        this.route = route;
        Scanner sc = new Scanner(System.in);
        int weight;
        boolean reqRefrig;
        System.out.println("Weight: (kg)");
        weight = sc.nextInt();
        System.out.println("Require refrigerated transport ? (false/true)");
        reqRefrig = sc.nextBoolean();
        this.weight = weight;
        this.requireRefrigeratedTransport = reqRefrig;
    }
}
