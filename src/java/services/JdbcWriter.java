package services;

import entity.*;
import org.javatuples.Triplet;
import javax.naming.NamingException;
import java.sql.*;
import java.util.HashMap;
import java.util.List;

public class JdbcWriter {
    private static JdbcWriter instance = null;

    final private String truckQuery;
    final private String routeQuery;
    final private String shippingQuery;
    final private String availableTruckQuery;

    final private Connection conn;

    private JdbcWriter() throws SQLException, ClassNotFoundException{
        Class.forName("com.mysql.cj.jdbc.Driver");

        String url = "jdbc:mysql://localhost:3306/pao_project?useTimezone=true&serverTimezone=UTC";
        String user = "paouser";
        String pass = "pao";
        conn = DriverManager.getConnection(url, user, pass);

        this.truckQuery = " insert into trucks (truck_type, registration_number, manufacturing_year)"
                + " values (?, ?, ?)";
        this.routeQuery = " insert into route (city1, city2, distance)"
                + " values (?, ?, ?)";
        this.shippingQuery = " insert into shipping (type, uuid, weight, refrigerated, city1, city2, distance, estimated_cost, shipping_bid, request_date, expiration_date, truck_number, unexpected_cost, final_profit, final_cost, date)"
                + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        this.availableTruckQuery = " insert into available_trucks (registration_number, available)"
                + " values (?, ?)";
    }

    public static JdbcWriter getInstance() throws SQLException, ClassNotFoundException, NamingException {
        if (instance == null) {
            instance = new JdbcWriter();
        }
        return instance;
    }

    public void writeScheduler(Scheduler scheduler) throws SQLException {
        writeRoutes(scheduler.getRoutes());
        writeTrucks(new Triplet<>(scheduler.getNormalTrucks(), scheduler.getRefrigeratedTrucks(),
                scheduler.getLargeTrucks()));
        writeShipping(new Triplet<>(scheduler.getRequestedShippings(), scheduler.getInProgressShippings(),
                scheduler.getFinishedShippings()));
        writeAvailableTrucks(scheduler.getAvailableTrucks());
        conn.close();
    }

    public void writeRoutes(List<Route> routes) throws SQLException {
        for (Route r: routes) {
            PreparedStatement preparedStatement = conn.prepareStatement(this.routeQuery);
            preparedStatement.setString(1, r.getCity1());
            preparedStatement.setString(2, r.getCity2());
            preparedStatement.setDouble(3, r.getDist());
            preparedStatement.execute();
        }
    }

    public void writeTrucks(Triplet<List<NormalTruck>, List<RefrigeratedTruck>, List<LargeTruck>> trucks) throws SQLException {
        for (NormalTruck t: trucks.getValue0()) {
            PreparedStatement preparedStatement = conn.prepareStatement(this.truckQuery);
            preparedStatement.setInt(1, 0);
            preparedStatement.setString(2, t.getRegistrationNumber());
            preparedStatement.setInt(3, t.getManufacturingYear());
            preparedStatement.execute();
        }
        for (RefrigeratedTruck t: trucks.getValue1()) {
            PreparedStatement preparedStatement = conn.prepareStatement(this.truckQuery);
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, t.getRegistrationNumber());
            preparedStatement.setInt(3, t.getManufacturingYear());
            preparedStatement.execute();
        }
        for (LargeTruck t: trucks.getValue2()) {
            PreparedStatement preparedStatement = conn.prepareStatement(this.truckQuery);
            preparedStatement.setInt(1, 2);
            preparedStatement.setString(2, t.getRegistrationNumber());
            preparedStatement.setInt(3, t.getManufacturingYear());
            preparedStatement.execute();
        }
    }

    public void writeShipping(Triplet<List<RequestedShipping>, List<InProgressShipping>, List<FinishedShipping>> shipping) throws SQLException {
        for (RequestedShipping s: shipping.getValue0()) {
            PreparedStatement preparedStatement = conn.prepareStatement(this.shippingQuery);
            preparedStatement.setInt(1, 0);
            preparedStatement.setString(2, s.getUuid());
            preparedStatement.setInt(3, s.getCargo().getWeight());
            preparedStatement.setBoolean(4, s.getCargo().isRequireRefrigeratedTransport());
            preparedStatement.setString(5, s.getCargo().getRoute().getCity1());
            preparedStatement.setString(6, s.getCargo().getRoute().getCity2());
            preparedStatement.setDouble(7, s.getCargo().getRoute().getDist());
            preparedStatement.setDouble(8, s.getEstimatedCosts());
            preparedStatement.setDouble(9, s.getShippingBid());
            preparedStatement.setString(10, s.getRequestDate());
            preparedStatement.setString(11, s.getExpirationDate());
            preparedStatement.setNull(12, Types.VARCHAR);
            preparedStatement.setNull(13, Types.DOUBLE);
            preparedStatement.setNull(14, Types.DOUBLE);
            preparedStatement.setNull(15, Types.DOUBLE);
            preparedStatement.setNull(16, Types.VARCHAR);
            preparedStatement.execute();
        }
        for (InProgressShipping s: shipping.getValue1()) {
            PreparedStatement preparedStatement = conn.prepareStatement(this.shippingQuery);
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, s.getUuid());
            preparedStatement.setInt(3, s.getCargo().getWeight());
            preparedStatement.setBoolean(4, s.getCargo().isRequireRefrigeratedTransport());
            preparedStatement.setString(5, s.getCargo().getRoute().getCity1());
            preparedStatement.setString(6, s.getCargo().getRoute().getCity2());
            preparedStatement.setDouble(7, s.getCargo().getRoute().getDist());
            preparedStatement.setDouble(8, s.getShippingBid());
            preparedStatement.setDouble(9, s.getEstimatedCosts());
            preparedStatement.setNull(10, Types.VARCHAR);
            preparedStatement.setNull(11, Types.VARCHAR);
            preparedStatement.setString(12, s.getTrucksRegistrationNumber());
            preparedStatement.setDouble(13, s.getUnexpectedCosts());
            preparedStatement.setNull(14, Types.DOUBLE);
            preparedStatement.setNull(15, Types.DOUBLE);
            preparedStatement.setNull(16, Types.VARCHAR);
            preparedStatement.execute();
        }
        for (FinishedShipping s: shipping.getValue2()) {
            PreparedStatement preparedStatement = conn.prepareStatement(this.shippingQuery);
            preparedStatement.setInt(1, 2);
            preparedStatement.setString(2, s.getUuid());
            preparedStatement.setInt(3, s.getCargo().getWeight());
            preparedStatement.setBoolean(4, s.getCargo().isRequireRefrigeratedTransport());
            preparedStatement.setString(5, s.getCargo().getRoute().getCity1());
            preparedStatement.setString(6, s.getCargo().getRoute().getCity2());
            preparedStatement.setDouble(7, s.getCargo().getRoute().getDist());
            preparedStatement.setDouble(8, s.getShippingBid());
            preparedStatement.setDouble(9, s.getEstimatedCosts());
            preparedStatement.setNull(10, Types.VARCHAR);
            preparedStatement.setNull(11, Types.VARCHAR);
            preparedStatement.setNull(12, Types.VARCHAR);
            preparedStatement.setNull(13, Types.DOUBLE);
            preparedStatement.setDouble(14, s.getFinalProfit());
            preparedStatement.setDouble(15, s.getFinalCosts());
            preparedStatement.setString(16, s.getDate());
            preparedStatement.execute();
        }
    }

    public void writeAvailableTrucks(HashMap<String, Boolean> availableTrucks) {
        availableTrucks.forEach((key, value) -> {
            try {
                PreparedStatement preparedStatement = conn.prepareStatement(this.availableTruckQuery);
                preparedStatement.setString(1, key);
                preparedStatement.setBoolean(2, value);
                preparedStatement.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

}
