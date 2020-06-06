package services;

import entity.*;
import org.javatuples.Triplet;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JdbcReader {
    private static JdbcReader instance = null;

    final private Connection conn;

    final private String truckQuery;
    final private String routeQuery;
    final private String shippingQuery;
    final private String availableTruckQuery;

    private JdbcReader() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        String url = "jdbc:mysql://localhost:3306/pao_project?useTimezone=true&serverTimezone=UTC";
        String user = "paouser";
        String pass = "pao";
        conn = DriverManager.getConnection(url, user, pass);
        truckQuery = "select * from trucks";
        routeQuery = "select * from route";
        shippingQuery = "select * from shipping";
        availableTruckQuery = "select * from available_trucks";
    }

    public static JdbcReader getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null) {
            instance = new JdbcReader();
        }
        return instance;
    }

    public void readScheduler() throws SQLException {
        List<Route> routes = readRoute();
        HashMap<String, Boolean> availableTrucks = readAvailableTrucks();
        Triplet<List<NormalTruck>, List<RefrigeratedTruck>, List<LargeTruck>> trucks = readTrucksFromCsv();
        Triplet<List<RequestedShipping>, List<InProgressShipping>, List<FinishedShipping>> shipping = readShipping();
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
        Statement statement = conn.createStatement();
        statement.executeUpdate("delete from route");
        statement.executeUpdate("delete from trucks");
        statement.executeUpdate("delete from shipping");
        statement.executeUpdate("delete from available_trucks");
        conn.close();
    }

    private List<Route> readRoute() throws SQLException {
        List<Route> routes = new ArrayList<>();
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(this.routeQuery);

        while (rs.next()) {
            Route r = new Route(rs.getString(1), rs.getString(2), rs.getDouble(3));
            routes.add(r);
        }

        return routes;
    }

    private Triplet<List<NormalTruck>, List<RefrigeratedTruck>, List<LargeTruck>> readTrucksFromCsv() throws SQLException {
        List<NormalTruck> normalTrucks = new ArrayList<>();
        List<RefrigeratedTruck> refrigeratedTrucks = new ArrayList<>();
        List<LargeTruck> largeTrucks = new ArrayList<>();
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(this.truckQuery);

        while(rs.next()) {
            switch (rs.getInt(1)) {
                case 0:
                    normalTrucks.add(new NormalTruck(rs.getString(2), rs.getInt(3)));
                    break;
                case 1:
                    refrigeratedTrucks.add(new RefrigeratedTruck(rs.getString(2), rs.getInt(3)));
                    break;
                case 2:
                    largeTrucks.add(new LargeTruck(rs.getString(2), rs.getInt(3)));
                    break;
                default:
                    break;
            }
        }

        return new Triplet<>(normalTrucks, refrigeratedTrucks, largeTrucks);
    }

    private HashMap<String, Boolean> readAvailableTrucks() throws SQLException {
        HashMap<String, Boolean> availability= new HashMap<>();
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(this.availableTruckQuery);
        while (rs.next()) {
            availability.put(rs.getString(1), rs.getBoolean(2));
        }
        return availability;
    }

    private Triplet<List<RequestedShipping>, List<InProgressShipping>, List<FinishedShipping>> readShipping() throws SQLException {
        List<RequestedShipping> requestedShipping = new ArrayList<>();
        List<InProgressShipping> inProgressShipping = new ArrayList<>();
        List<FinishedShipping> finishedShipping = new ArrayList<>();
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(this.shippingQuery);
        while (rs.next()) {
            String uuid = rs.getString(2);
            Cargo cargo = new Cargo(rs.getInt(3), rs.getBoolean(4),
                    new Route(rs.getString(5), rs.getString(6), rs.getDouble(7)));
            double estimatedCosts = rs.getDouble(8);
            double shippingBid = rs.getDouble(9);
            switch (rs.getInt(1)) {
                case 0:
                    String rd = rs.getString(10);
                    String ed = rs.getString(11);
                    requestedShipping.add(new RequestedShipping(uuid, cargo, estimatedCosts, shippingBid, rd, ed));
                    break;
                case 1:
                    String truckN = rs.getString(12);
                    double ue = rs.getDouble(13);
                    inProgressShipping.add(new InProgressShipping(uuid, cargo, estimatedCosts, shippingBid, truckN, ue));
                    break;
                case 2:
                    double fp = rs.getDouble(14);
                    double fc = rs.getDouble(15);
                    String date = rs.getString(16);
                    finishedShipping.add(new FinishedShipping(uuid, cargo, estimatedCosts, shippingBid, fp, fc, date));
                    break;
                default:
                    break;
            }
        }
        return new Triplet<>(requestedShipping, inProgressShipping, finishedShipping);
    }
}
