package services;

import entity.*;
import org.javatuples.Triplet;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CsvReader {
    private static CsvReader instance = null;

    final private String routesFileName;
    final private String trucksFilename;
    final private String shippingFilename;
    final private String availableTrucksFilename;


    private CsvReader() {
        routesFileName = "csv/routes.csv";
        trucksFilename = "csv/trucks.csv";
        shippingFilename = "csv/shipping.csv";
        availableTrucksFilename = "csv/availableTrucks.csv";
    }

    public static CsvReader getInstance() {
        if (instance == null) {
            instance = new CsvReader();
        }
        return instance;
    }

    public void readScheduler() {
        List<Route> routes = readRouteFromCsv();
        HashMap<String, Boolean> availableTrucks = readAvailableTrucks();
        Triplet<List<NormalTruck>, List<RefrigeratedTruck>, List<LargeTruck>> trucks = readTrucksFromCsv();
        Triplet<List<RequestedShipping>, List<InProgressShipping>, List<FinishedShipping>> shipping = readShippingFromCsv();
        if (!routes.isEmpty() || !availableTrucks.isEmpty() ||
            !trucks.getValue0().isEmpty() || !trucks.getValue1().isEmpty() || trucks.getValue2().isEmpty() ||
            !shipping.getValue0().isEmpty() || !shipping.getValue1().isEmpty() || !shipping.getValue2().isEmpty()) {

            Scheduler scheduler = Scheduler.getInstance();
            scheduler.setRoutes(routes);
            scheduler.setNormalTrucks(trucks.getValue0());
            scheduler.setRefrigeratedTrucks(trucks.getValue1());
            scheduler.setLargeTrucks(trucks.getValue2());
            scheduler.setRequestedShippings(shipping.getValue0());
            scheduler.setInProgressShippings(shipping.getValue1());
            scheduler.setFinishedShippings(shipping.getValue2());
            scheduler.setAvailableTrucks(availableTrucks);
        }
    }

    private List<Route> readRouteFromCsv() {
        List<Route> routes = new ArrayList<>();
        Path pathToFile = Paths.get(instance.routesFileName);

        try (BufferedReader br = Files.newBufferedReader(pathToFile)) {
            // read first line
            br.readLine();

            String line = br.readLine();

            // loop until all lines are read
            while (line != null) {
                String[] attributes = line.split(",");
                Route route = this.createRoute(attributes);
                routes.add(route);
                line = br.readLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return routes;
    }

    private HashMap<String, Boolean> readAvailableTrucks() {
        HashMap<String, Boolean> availability= new HashMap<>();
        Path pathToFile = Paths.get(this.availableTrucksFilename);

        try (BufferedReader br = Files.newBufferedReader(pathToFile)) {
//            read first line
            br.readLine();

            String line = br.readLine();
            while (line != null) {
                String[] attributes = line.split(",");
                availability.put(attributes[0], Boolean.parseBoolean(attributes[1]));
                line = br.readLine();
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return availability;
    }

    private Triplet<List<NormalTruck>, List<RefrigeratedTruck>, List<LargeTruck>> readTrucksFromCsv() {
        List<NormalTruck> normalTrucks = new ArrayList<>();
        List<RefrigeratedTruck> refrigeratedTrucks = new ArrayList<>();
        List<LargeTruck> largeTrucks = new ArrayList<>();
        Path pathToFile = Paths.get(this.trucksFilename);

        try (BufferedReader br = Files.newBufferedReader(pathToFile)){
            br.readLine();
            String line = br.readLine();
            while (line != null) {
                String[] attributes = line.split(",");
                int type = Integer.parseInt(attributes[0]);
                String[] metadata = Arrays.copyOfRange(attributes, 1, attributes.length);
                switch (type) {
                    case 0:
                        normalTrucks.add(this.createTruck(metadata, type));
                        break;
                    case 1:
                        refrigeratedTrucks.add(this.createTruck(metadata, type));
                        break;
                    case 2:
                        largeTrucks.add(this.createTruck(metadata, type));
                        break;
                    default:
                        break;
                }
                line = br.readLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return new Triplet<>(normalTrucks, refrigeratedTrucks, largeTrucks);
    }

    private Triplet<List<RequestedShipping>, List<InProgressShipping>, List<FinishedShipping>> readShippingFromCsv() {
        List<RequestedShipping> requestedShipping = new ArrayList<>();
        List<InProgressShipping> inProgressShipping = new ArrayList<>();
        List<FinishedShipping> finishedShipping = new ArrayList<>();
        Path pathToFile = Paths.get(this.shippingFilename);

        try (BufferedReader br = Files.newBufferedReader(pathToFile)){
            br.readLine();
            String line = br.readLine();
            while (line != null) {
                String[] attributes = line.split(",");
                int type = Integer.parseInt(attributes[0]);
                String[] aux = Arrays.copyOfRange(attributes, 1, attributes.length);
                String[] metadata = Arrays.stream(aux)
                        .filter(value ->
                                value != null && value.length() > 0
                        )
                        .toArray(String[]::new);
                System.out.println(Arrays.toString(metadata));
                switch (type) {
                    case 0:
                        requestedShipping.add(this.createShipping(metadata, type));
                        break;
                    case 1:
                        inProgressShipping.add(this.createShipping(metadata, type));
                        break;
                    case 2:
                        finishedShipping.add(this.createShipping(metadata, type));
                        break;
                }
                line = br.readLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return new Triplet<>(requestedShipping, inProgressShipping, finishedShipping);
    }

    private Route createRoute(String[] metadata) {
        String city1 = metadata[0];
        String city2 = metadata[1];
        double dist = Double.parseDouble(metadata[2]);
        return new Route(city1, city2, dist);
    }

    private Cargo createCargo(String[] metadata) {
        int weight = Integer.parseInt(metadata[0]);
        boolean requireRefrigeratedTransport = Boolean.parseBoolean(metadata[1]);
        Route route = createRoute(Arrays.copyOfRange(metadata, 2, 5));
        return new Cargo(weight, requireRefrigeratedTransport, route);
    }

    private ShippingRequest createShippingRequest(String[] metadata) {
        Cargo cargo = createCargo(Arrays.copyOfRange(metadata, 0, 5));
        double bid = Double.parseDouble(metadata[5]);
        return new ShippingRequest(cargo, bid);
    }

    private <T extends Truck> T createTruck(String[] metadata, int truckType) {
        String registrationNumber = metadata[0];
        int manufacturingYear = Integer.parseInt(metadata[1]);
        switch (truckType){
            case 0:
                return (T) (new NormalTruck(registrationNumber, manufacturingYear));
            case 1:
                return (T) (new RefrigeratedTruck(registrationNumber, manufacturingYear));
            case 2:
                return (T) (new LargeTruck(registrationNumber, manufacturingYear));
            default:
               throw new IllegalArgumentException("invalid truck type");
        }
    }

    private <T extends Shipping> T createShipping(String[] metadata, int shippingType) {
//        base class attributes
        String uuid = metadata[0];
        Cargo cargo = createCargo(Arrays.copyOfRange(metadata, 1, 6));
        double estimatedCosts = Double.parseDouble(metadata[6]);
        double shippingBid = Double.parseDouble(metadata[7]);

        switch (shippingType) {
            case 0:
                String requestDate = metadata[8];
                String expirationDate = metadata[9];
                return (T) new RequestedShipping(uuid, cargo, estimatedCosts, shippingBid, requestDate, expirationDate);
            case 1:
                String truckRegistrationNumber = metadata[8];
                double unexpectedCosts = Double.parseDouble(metadata[9]);
                System.out.println(unexpectedCosts);
                return (T) new InProgressShipping(uuid, cargo, estimatedCosts, shippingBid, truckRegistrationNumber, unexpectedCosts);
            case 2:
                double finalProfit = Double.parseDouble(metadata[8]);
                double finalCosts = Double.parseDouble(metadata[9]);
                String date = metadata[10];
                return (T) new FinishedShipping(uuid, cargo, estimatedCosts, shippingBid, finalProfit, finalCosts, date);
            default:
                throw new IllegalArgumentException("invalid shipping type");
        }
    }
}
