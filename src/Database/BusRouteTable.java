package Database;

import Main.MainRun;
import constant.Database;
import entity.BusRoute;
import entity.Driver;
import util.DatabaseConnection;
import util.ObjectUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class BusRouteTable {
    public static final String BUSROUTE_TABLE_NAME = "BUSROUTE";
    private static final Connection connection;
    public static final String ID = "ids";
    public static final String DISTANCES = "distances";
    public static final String BUSSTOP = "busStop";

    static {
        connection = DatabaseConnection.openConnection(Database.DRIVER_STRING, Database.URL, Database.USERNAME, Database.PASSWORD);
    }

    public static void createTable() {

        DatabaseMetaData dbm = null;
        try {
            dbm = connection.getMetaData();
            ResultSet tables = dbm.getTables(null, Database.USERNAME, BusRouteTable.BUSROUTE_TABLE_NAME, null);
            if (!tables.next()) {
                try {
                    PreparedStatement busRoute = connection.prepareStatement("CREATE TABLE " + BUSROUTE_TABLE_NAME + "("
                            + "Ids number primary key, "
                            + "Distances number NOT NULL, "
                            + "BusStop number NOT NULL)");
                    busRoute.executeQuery();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static List<BusRoute> getBusRoutes() {
        List<BusRoute> busRoutes = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM " + BUSROUTE_TABLE_NAME + " ORDER BY ids";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            busRoutes = new ArrayList();
            while (resultSet.next()) {
                float distance = resultSet.getFloat(DISTANCES);
                int busStop = resultSet.getInt(BUSSTOP);
                BusRoute busRoute = new BusRoute(distance, busStop);
                busRoutes.add(busRoute);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(resultSet, preparedStatement, null);
        }
        return busRoutes;
    }

    public static void insertNewBusRoute(BusRoute busRoute) {
        if (ObjectUtil.isEmpty(busRoute)) {
            return;
        }
        PreparedStatement preparedStatement = null;
        try {
            String query = "INSERT INTO " + BUSROUTE_TABLE_NAME + " VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, busRoute.getId());
            preparedStatement.setFloat(2, busRoute.getDistance());
            preparedStatement.setInt(3, busRoute.getStopStationNumber());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(null, preparedStatement, null);
        }
    }

    public static void createNewBusRoute() {
        System.out.print("B???n mu???n nh???p th??m m???y tuy???n m???i: ");
        int newDriverNumber = 0;
        do {
            try {
                newDriverNumber = new Scanner(System.in).nextInt();
            } catch (InputMismatchException ex) {
                System.out.print("S??? l?????ng tuy???n ph???i l?? s??? nguy??n, y??u c???u nh???p l???i: ");
                continue;
            }
            if (newDriverNumber > 0) {
                break;
            }
            System.out.print("S??? l?????ng tuy???n KH??NG ???????c l?? s??? ??m, y??u c???u nh???p l???i: ");
        } while (true);
        for (int i = 0; i < newDriverNumber; i++) {
            System.out.println("Nh???p th??ng tin tuy???n th??? " + (i + 1));
            BusRoute busRoute = new BusRoute();
            busRoute.inputNewData();
            insertNewBusRoute(busRoute);
            MainRun.BUS_ROUTES.add(busRoute);
        }
    }

    public static void showBusRoute() {
        if (MainRun.BUS_ROUTES != null)
            MainRun.BUS_ROUTES.forEach(System.out::println);
    }

    public static BusRoute findById(int busRouteId) {
        return MainRun.BUS_ROUTES
                .stream()
                .filter(busRoute -> busRoute.getId() == busRouteId)
                .findFirst()
                .orElse(null);
    }
}
