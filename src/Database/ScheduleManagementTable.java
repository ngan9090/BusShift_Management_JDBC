package Database;

import Main.MainRun;
import constant.Database;
import entity.BusRoute;
import entity.Distance;
import entity.Driver;
import entity.ScheduleManagement;
import util.DatabaseConnection;
import util.ObjectUtil;

import java.sql.*;
import java.util.*;

public class ScheduleManagementTable {
    public static final String SCHEDULE_TABLE_NAME = "SCHEDULE_MANAGEMENT";
    private static final Connection connection;
    public static final String ID = "ids";
    public static final String DRIVER_ID = "driverId";
    public static final String BUS_ROUTE_ID = "busRouteId";
    public static final String TURN_NUMBER = "turnNumber";

    static {
        connection = DatabaseConnection.openConnection(Database.DRIVER_STRING, Database.URL, Database.USERNAME, Database.PASSWORD);
    }

    public static void createTable() {

        DatabaseMetaData dbm = null;
        try {
            dbm = connection.getMetaData();
            ResultSet tables = dbm.getTables(null, Database.USERNAME, ScheduleManagementTable.SCHEDULE_TABLE_NAME, null);
            if (!tables.next()) {
                try {
                    PreparedStatement busRoute = connection.prepareStatement("CREATE TABLE " + SCHEDULE_TABLE_NAME + "("
                            + "ids number primary key, "
                            + "driverId number not null constraint driverId references driver(ids), "
                            + "BusRouteId number not null constraint BusRouteId references busRoute(ids),"
                            + "turnNumber number not null)");
                    busRoute.executeQuery();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static List<ScheduleManagement> getScheduleManagements() {
        List<ScheduleManagement> scheduleManagements = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM " + SCHEDULE_TABLE_NAME + " ORDER BY ids";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            scheduleManagements = new ArrayList();
            while (resultSet.next()) {
                int driver_Id = resultSet.getInt(DRIVER_ID);
                int bus_Route_Id = resultSet.getInt(BUS_ROUTE_ID);
                int turn_number = resultSet.getInt(TURN_NUMBER);
                ScheduleManagement scheduleManagement = new ScheduleManagement(driver_Id, bus_Route_Id, turn_number);
                scheduleManagements.add(scheduleManagement);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(resultSet, preparedStatement, null);
        }
        return scheduleManagements;
    }

    public static void insertNewSchedule(ScheduleManagement scheduleManagement) {
        if (ObjectUtil.isEmpty(scheduleManagement)) {
            return;
        }
        PreparedStatement preparedStatement = null;
        try {
            String query = "INSERT INTO " + SCHEDULE_TABLE_NAME + " VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, scheduleManagement.getId());
            preparedStatement.setInt(2, scheduleManagement.getDriverId());
            preparedStatement.setInt(3, scheduleManagement.getBusRouteId());
            preparedStatement.setInt(4, scheduleManagement.getTurnNumber());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(null, preparedStatement, null);
        }
    }

    public static void createNewSchedule() {
        System.out.print("B???n mu???n nh???p th??m m???y ph??n c??ng m???i: ");
        int newScheduleNumber = 0;
        do {
            try {
                newScheduleNumber = new Scanner(System.in).nextInt();
            } catch (InputMismatchException ex) {
                System.out.print("S??? l?????ng l???ch tr??nh ph???i l?? s??? nguy??n, y??u c???u nh???p l???i: ");
                continue;
            }
            if (newScheduleNumber > 0) {
                break;
            }
            System.out.print("S??? l?????ng l???ch tr??nh KH??NG ???????c l?? s??? ??m, y??u c???u nh???p l???i: ");
        } while (true);
        for (int i = 0; i < newScheduleNumber; i++) {
            System.out.println("Nh???p th??ng tin l???ch tr??nh th??? " + (i + 1));
            ScheduleManagement scheduleManagement = new ScheduleManagement();
            scheduleManagement.inputNewData();
            insertNewSchedule(scheduleManagement);
            MainRun.SCHEDULE_MANAGEMENTS.add(scheduleManagement);
        }
    }

    public static void showSchedule() {
        MainRun.SCHEDULE_MANAGEMENTS.forEach(System.out::println);
    }

    public static ScheduleManagement checkUnique(int driverId, int busRouteId) {
        return MainRun.SCHEDULE_MANAGEMENTS
                .stream()
                .filter(scheduleManagement -> scheduleManagement.getDriverId() == driverId)
                .filter(scheduleManagement -> scheduleManagement.getBusRouteId() == busRouteId)
                .findFirst()
                .orElse(null);
    }

    public static int checkLimitTurn(int turnNumber, int driverId) {
        return (int) MainRun.SCHEDULE_MANAGEMENTS
                .stream()
                .filter(scheduleManagement -> scheduleManagement.getDriverId() == driverId)
                .mapToDouble(ScheduleManagement::getTurnNumber)
                .reduce(0, (value1, value2) -> value1 + value2) + turnNumber;

    }

    //S???p s???p
    public static void sortRouteManagerment() {
        System.out.println("S???p s???p danh s??ch ph??n c??ng l??i xe");
        System.out.println("1.Theo t??n l??i xe");
        System.out.println("2.T???ng s??? tuy???n gi???m d???n");
        System.out.print("Nh???p s??? l???a ch???n c???a b???n: ");
        int turn = 0;
        do {
            try {
                turn = new Scanner(System.in).nextInt();
            } catch (InputMismatchException e) {
                System.out.print("L???a ch???n s???p s???p danh s??ch ph???i l?? s??? nguy??n, y??u c???u nh???p l???i: ");
                continue;
            }
            if (turn > 0 && turn < 3) break;
            System.out.println("L???a ch???n c???n nh???p t??? 1 ?????n 2. Xin vui l??ng nh???p l???i: ");
        } while (true);

        switch (turn) {
            case 1:
                sortByDriverName();
                break;
            case 2:
                sortRouteManagermentByTotal();
                break;
        }
    }

    //GetName
    public static void sortByDriverName() {
        List<ScheduleManagement> scheduleManagements = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM " + SCHEDULE_TABLE_NAME + " s "
                    +"JOIN " + DriverTable.DRIVER_TABLE_NAME +" d "
                    + "ON s.driverid = d.ids "
                    + "ORDER BY d.names";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            scheduleManagements = new ArrayList();
            while (resultSet.next()) {
                int driver_Id = resultSet.getInt(DRIVER_ID);
                int bus_Route_Id = resultSet.getInt(BUS_ROUTE_ID);
                int turn_number = resultSet.getInt(TURN_NUMBER);
                ScheduleManagement scheduleManagement = new ScheduleManagement(driver_Id, bus_Route_Id, turn_number);
                scheduleManagements.add(scheduleManagement);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(resultSet, preparedStatement, null);
        }
        scheduleManagements.forEach(System.out::println);
    }

    public static void sortRouteManagermentByTotal() {
        MainRun.SCHEDULE_MANAGEMENTS.sort((ScheduleManagement s1, ScheduleManagement s2) -> s2.getTurnNumber() - s1.getTurnNumber());
        MainRun.SCHEDULE_MANAGEMENTS.forEach(System.out::println);
    }

    public static void calculateTotalRound() {
        System.out.println("--------------------------------");
        List<Distance> distanceTotals = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT s.driverId,d.names, sum(s.turnnumber*b.distances) distance FROM " + SCHEDULE_TABLE_NAME + " s "
                    + "JOIN " + DriverTable.DRIVER_TABLE_NAME +" d "
                    + "ON s.driverid = d.ids "
                    + "JOIN " + BusRouteTable.BUSROUTE_TABLE_NAME +" b "
                    + "ON s.busrouteid = b.ids "
                    + "GROUP BY s.driverid, d.names "
                    + "ORDER BY sum(s.turnnumber*b.distances) DESC";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            distanceTotals = new ArrayList();
            while (resultSet.next()) {
                int driver_Id = resultSet.getInt(DRIVER_ID);
                String driver_name = resultSet.getString("names");
                float distance = resultSet.getFloat("distance");
                Distance distanceTotal = new Distance(driver_Id, driver_name, distance);
                distanceTotals.add(distanceTotal);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(resultSet, preparedStatement, null);
        }
        distanceTotals.forEach(System.out::println);
        System.out.println("--------------------------------");
    }

}
