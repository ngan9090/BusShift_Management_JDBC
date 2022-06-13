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
    public static void createTable(){

        DatabaseMetaData dbm = null;
        try {
            dbm = connection.getMetaData();
            ResultSet tables = dbm.getTables(null, "SQL4", BusRouteTable.BUSROUTE_TABLE_NAME, null);
            if(!tables.next()) {
                try {
                    PreparedStatement busRoute = connection.prepareStatement("CREATE TABLE SQL4.BusRoute("
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
            String query = "SELECT * FROM SQL4." + BUSROUTE_TABLE_NAME + " ORDER BY ids";
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
            String query = "INSERT INTO SQL4.BUSROUTE VALUES (?, ?, ?)";
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
        System.out.print("Bạn muốn nhập thêm mấy tuyến mới: ");
        int newDriverNumber = 0;
        do {
            try {
                newDriverNumber = new Scanner(System.in).nextInt();
            } catch (InputMismatchException ex) {
                System.out.print("Số lượng tuyến phải là số nguyên, yêu cầu nhập lại: ");
                continue;
            }
            if (newDriverNumber > 0) {
                break;
            }
            System.out.print("Số lượng tuyến KHÔNG được là số âm, yêu cầu nhập lại: ");
        } while (true);
        for (int i = 0; i < newDriverNumber; i++) {
            System.out.println("Nhập thông tin tuyến thứ " + (i+1));
            BusRoute busRoute = new BusRoute();
            busRoute.inputNewData();
            insertNewBusRoute(busRoute);
            MainRun.BUS_ROUTES.add(busRoute);
        }
    }

    public static void showBusRoute() {
        if(MainRun.BUS_ROUTES != null)
        MainRun.BUS_ROUTES.forEach(System.out::println);
    }
}
