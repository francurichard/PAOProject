package services;

import entity.*;
import org.javatuples.Triplet;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class CsvWriter {
    private static CsvWriter instance = null;

    final private String routesFilename;
    final private String trucksFilename;
    final private String shippingFilename;
    final private String availableTrucksFilename;

    private CsvWriter() {
        routesFilename = "csv/routes.csv";
        trucksFilename = "csv/trucks.csv";
        shippingFilename = "csv/shipping.csv";
        availableTrucksFilename = "csv/availableTrucks.csv";
    }

    public static CsvWriter getInstance() {
        if (instance == null) {
            instance = new CsvWriter();
        }
        return instance;
    }

    public void writeScheduler(Scheduler scheduler) {
        writeRouteToCsv(scheduler.getRoutes());
        writeTrucksToCsv(new Triplet<>(scheduler.getNormalTrucks(), scheduler.getRefrigeratedTrucks(),
                scheduler.getLargeTrucks()));
        writeShippingToCsv(new Triplet<>(scheduler.getRequestedShippings(), scheduler.getInProgressShippings(),
                scheduler.getFinishedShippings()));
        writeAvailableTrucks(scheduler.getAvailableTrucks());
    }

    public void writeRouteToCsv(List<Route> routes) {
        Path pathToFile = Paths.get(this.routesFilename);

        try (BufferedWriter wr = Files.newBufferedWriter(pathToFile)) {
            wr.write("city1,city2,distance");
            wr.newLine();
            for (Route r: routes) {
                wr.write(r.toCsv());
                wr.newLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void writeShippingToCsv(Triplet<List<RequestedShipping>, List<InProgressShipping>, List<FinishedShipping>> shipping) {
        Path pathToFile = Paths.get(this.shippingFilename);

        try (BufferedWriter wr = Files.newBufferedWriter(pathToFile)) {
            wr.write("type,uuid,weight,required refrigerated transport,city1,city2,dist,estimated costs,shipping bid,request date, expiration date, truck registration number,unexpected costs,final profit, final costs, date");
            wr.newLine();

            for (RequestedShipping s: shipping.getValue0()) {
                wr.write(s.toCsv());
                wr.newLine();
            }

            for (InProgressShipping s: shipping.getValue1()) {
                wr.write(s.toCsv());
                wr.newLine();
            }

            for (FinishedShipping s: shipping.getValue2()) {
                wr.write(s.toCsv());
                wr.newLine();
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void writeTrucksToCsv(Triplet<List<NormalTruck>, List<RefrigeratedTruck>, List<LargeTruck>> trucks) {
        Path pathToFile = Paths.get(this.trucksFilename);

        try (BufferedWriter wr = Files.newBufferedWriter(pathToFile)) {
            wr.write("java.entity.Truck Type, Registration Number, Manufacturing Year");
            wr.newLine();
            for (NormalTruck t: trucks.getValue0()) {
                wr.write("0," + t.toCsv());
                wr.newLine();
            }
            for (RefrigeratedTruck t: trucks.getValue1()) {
                wr.write("1," + t.toCsv());
                wr.newLine();
            }
            for (LargeTruck t: trucks.getValue2()) {
                wr.write("2," + t.toCsv());
                wr.newLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void writeAvailableTrucks(HashMap<String, Boolean> availableTrucks) {
        Path path = Paths.get(this.availableTrucksFilename);

        try (BufferedWriter wr = Files.newBufferedWriter(path)) {
            wr.write("registration number,availability");
            wr.newLine();
            availableTrucks.forEach((key, value) -> {
                try {
                    wr.write(key+","+value);
                    wr.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
