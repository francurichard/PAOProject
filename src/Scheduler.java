import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Scheduler implements Serializable {

    private static volatile Scheduler sScheduler;

    private static double minProfit = 0.15;

    private List<NormalTruck> normalTrucks;
    private List<RefrigeratedTruck> refrigeratedTrucks;
    private List<LargeTruck> largeTrucks;

    private List<Route> routes;

    private List<RequestedShipping> requestedShippings;
    private List<InProgressShipping> inProgressShippings;
    private List<FinishedShipping> finishedShippings;

    HashMap<String, Boolean> availableTrucks;
//    private constructor
    private Scheduler() {
//        prevent from the reflection api
        if (sScheduler != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

        this.largeTrucks = new ArrayList<>();
        this.normalTrucks = new ArrayList<>();
        this.refrigeratedTrucks = new ArrayList<>();

        this.routes = new ArrayList<>();

        this.requestedShippings = new ArrayList<>();
        this.inProgressShippings = new ArrayList<>();
        this.finishedShippings = new ArrayList<>();

        this.availableTrucks = new HashMap<>();
    }

    static Scheduler getInstance() {
        if (sScheduler == null) {
            synchronized (Scheduler.class) {
                if (sScheduler == null) sScheduler = new Scheduler();
            }
        }
        return sScheduler;
    }

    protected Scheduler readResolve() {
        return getInstance();
    }

    protected void addNewTruck(Truck truck, int type) {
        switch (type) {
            case 1:
                this.normalTrucks.add(new NormalTruck(truck.getRegistrationNumber(), truck.getManufacturingYear()));
                break;
            case 2:
                this.refrigeratedTrucks.add(new RefrigeratedTruck(truck.getRegistrationNumber(), truck.getManufacturingYear()));
                break;
            case 3:
                this.largeTrucks.add(new LargeTruck(truck.getRegistrationNumber(), truck.getManufacturingYear()));
                break;
            default:
                break;
        }
        this.availableTrucks.put(truck.getRegistrationNumber(), true);
    }


    protected void listAvailableTrucks() {
        if (!this.normalTrucks.isEmpty()) {
            System.out.println("Normal trucks:");
            for (NormalTruck truck: this.normalTrucks) {
                if (availableTrucks.get(truck.getRegistrationNumber())) {
                    System.out.println(truck);
                }
            }
        }

        if (!this.refrigeratedTrucks.isEmpty()) {
            System.out.println("Refrigerated trucks:");
            for (RefrigeratedTruck truck: this.refrigeratedTrucks) {
                if (availableTrucks.get(truck.getRegistrationNumber())) {
                    System.out.println(truck);
                }
            }
        }

        if (!this.largeTrucks.isEmpty()) {
            System.out.println("Large trucks:");
            for (LargeTruck truck: this.largeTrucks) {
                if (availableTrucks.get(truck.getRegistrationNumber())) {
                    System.out.println(truck);
                }
            }
        }
    }

    protected void addNewRoute(Route route) {
//        First check if route already exists, if yes, then update distance, else add route to list
        if (this.routes.contains(route)) {
            for (int i = 0; i < this.routes.size(); i++) {
                if (this.routes.get(i).equals(route)) {
                    this.routes.get(i).setDist(route.getDist());
                }
            }
        } else {
            this.routes.add(route);
        }
    }

    protected boolean checkIfRouteExists(Route route) {
        return this.routes.contains(route);
    }

    protected double calculateShippingCosts(Cargo cargo) {
        double dist = 0;
        double pricePerKm = 0;
        int truckType;
        for (int i = 0; i < this.routes.size(); i++) {
            if (routes.get(i).equals(cargo.getRoute())) {
                dist = routes.get(i).getDist();
            }
        }
        truckType = cargo.getNecessaryTruck();
        switch (truckType) {
            case 1:
                pricePerKm = NormalTruck.pricePerKm;
                break;
            case 2:
                pricePerKm = RefrigeratedTruck.pricePerKm;
                break;
            case 3:
                pricePerKm = LargeTruck.pricePerKm;
                break;
            default:
                break;
        }
        return dist * pricePerKm;
    }

    protected double calculateShippingPrice(Cargo cargo) {
        double costs = calculateShippingCosts(cargo);
        return costs + costs * minProfit;
    }

    protected RequestResponse acceptShippingRequest(ShippingRequest request) {
        if (checkIfRouteExists(request.getCargo().getRoute())) {
            double price = calculateShippingPrice(request.cargo);
            if (price <= request.getBid()) {
                double costs = calculateShippingCosts(request.cargo);
                RequestedShipping shipping = new RequestedShipping(request.cargo, costs, request.bid);
                this.requestedShippings.add(shipping);
                return new RequestResponse(shipping.getUuid(), true);
            } else {
                return new RequestResponse(null, false);
            }
        } else {
            return new RequestResponse(null, false);
        }
    }

    protected String ShippingIsFinished(String uuid) {
//        if uuid is valid then it will return registration number of the truck, is useful for new configuration
        String registrationNumber = "";
        int pos = 0;
        for (int i = 0; i < this.inProgressShippings.size(); i++) {
            if (uuid.equals(inProgressShippings.get(i).getUuid())) {
                registrationNumber = inProgressShippings.get(i).getTrucksRegistrationNumber();
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                LocalDateTime now = LocalDateTime.now();
                InProgressShipping finishedShipping = inProgressShippings.get(i);
                pos = i;
                this.finishedShippings.add(new FinishedShipping(finishedShipping.getCargo(), finishedShipping.getEstimatedCosts(), finishedShipping.getShippingBid(), dtf.format(now), finishedShipping.getUnexpectedCosts()));
            }
        }
        if (registrationNumber != "") {
            this.inProgressShippings.remove(pos);
        }
        return registrationNumber;
    }

    protected void markTruckAsAvailable(String registrationNumber) {
        this.availableTrucks.put(registrationNumber, true);
    }

    protected boolean updateShippingCost(String uuid, double cost) {
        boolean res = false;
        for (int i = 0; i < this.inProgressShippings.size(); i++) {
            if (this.inProgressShippings.get(i).getUuid().equals(uuid)) {
                res = true;
                this.inProgressShippings.get(i).addCost(cost);
            }
        }
        return res;
    }

    protected double calculateProfit() {
        double res = 0;
        for (int i = 0; i < this.finishedShippings.size(); i++) {
            res = res + this.finishedShippings.get(i).getFinalProfit();
        }
        return res;
    }

    protected void listFinishedShippingsMonth() {
        for (int i = 0; i < this.finishedShippings.size(); i++) {
            System.out.println(this.finishedShippings.get(i));
        }
    }

    protected String findTruck(int type) {
        String res = "";
        System.out.println(type);
        switch (type) {
            case 1: {
                for (int i = 0; i < this.normalTrucks.size(); i++) {
                    NormalTruck truck = this.normalTrucks.get(i);
                    if (this.availableTrucks.get(truck.getRegistrationNumber())) {
                        res = truck.getRegistrationNumber();
                        return res;
                    }
                }
                break;
            }
            case 2: {
                for (int i = 0; i < this.refrigeratedTrucks.size(); i++) {
                    RefrigeratedTruck truck = this.refrigeratedTrucks.get(i);
                    if (this.availableTrucks.get(truck.getRegistrationNumber())) {
                        res = truck.getRegistrationNumber();
                        return res;
                    }
                }
                break;
            }

            case 3: {
                for (int i = 0; i < this.largeTrucks.size(); i++) {
                    LargeTruck truck = this.largeTrucks.get(i);
                    if (this.availableTrucks.get(truck.getRegistrationNumber())) {
                        res = truck.getRegistrationNumber();
                        return res;
                    }
                }
                break;
            }

        }
        return res;

    }

    protected RequestResponse scheduleShipping(String uuid) {
        int pos = 0;
        boolean res = false;
        String newUuid = "";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime date = LocalDateTime.now();
        for (int i = 0; i < this.requestedShippings.size(); i++) {
            if (this.requestedShippings.get(i).getUuid().equals(uuid)) {
                RequestedShipping shipping = this.requestedShippings.get(i);
                String truck = findTruck(shipping.getCargo().getNecessaryTruck());
                if (truck != "") {
                    InProgressShipping ship = new InProgressShipping(shipping.getCargo(), shipping.getEstimatedCosts(), shipping.getShippingBid(), truck);
                    newUuid = ship.getUuid();
                    this.inProgressShippings.add(ship);
                    this.availableTrucks.put(truck, false);
                    pos = i;
                    res = true;
                }
            }
        }

        if (res) {
            this.requestedShippings.remove(pos);
        }
        return new RequestResponse(newUuid, res);
    }

}






