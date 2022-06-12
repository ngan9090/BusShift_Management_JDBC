package Database;

import Main.MainRun;
import constant.Database;
import entity.Driver;
import util.CollectionUtil;
import util.DatabaseConnection;
import util.ObjectUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class DriverTable {
    public static final String DRIVER_TABLE_NAME = "DRIVER";

    public static final String ID = "ids";
    public static final String NAME = "names";
    public static final String ADDRESS = "address";
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String TYPE = "levels";

    private static final Connection connection;

    static {
        connection = DatabaseConnection.openConnection(Database.DRIVER_STRING, Database.URL, Database.USERNAME, Database.PASSWORD);
    }


    public static List<Driver> getDrivers() {
        List<Driver> drivers = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM SQL4." + DRIVER_TABLE_NAME + " ORDER BY ids";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            drivers = new ArrayList();
            while (resultSet.next()) {
                String name = resultSet.getString(NAME);
                String address = resultSet.getString(ADDRESS);
                String phone_number = resultSet.getString(PHONE_NUMBER);
                String type = resultSet.getString(TYPE);
                Driver driver = new Driver(name, address, phone_number, type);
                drivers.add(driver);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(resultSet, preparedStatement, null);
        }
        return drivers;
    }

    public static void insertNewDriver(Driver driver) {
        if (ObjectUtil.isEmpty(driver)) {
            return;
        }
        PreparedStatement preparedStatement = null;
        try {
            String query = "INSERT INTO SQL4.DRIVER VALUES (?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);
           preparedStatement.setInt(1, driver.getId());
           preparedStatement.setString(2, driver.getName());
            preparedStatement.setString(3, driver.getAddress());
            preparedStatement.setString(4, driver.getPhone());
            preparedStatement.setString(5, driver.getLevel());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(null, preparedStatement, null);
        }
    }

    public static void createNewDriver() {
        System.out.print("Bạn muốn nhập thêm mấy lái xe mới: ");
        int newDriverNumber = 0;
        do {
            try {
                newDriverNumber = new Scanner(System.in).nextInt();
            } catch (InputMismatchException ex) {
                System.out.print("Số lượng tài xế phải là số nguyên, yêu cầu nhập lại: ");
                continue;
            }
            if (newDriverNumber > 0) {
                break;
            }
            System.out.print("Số lượng tài xế KHÔNG được là số âm, yêu cầu nhập lại: ");
        } while (true);
        for (int i = 0; i < newDriverNumber; i++) {
            System.out.println("Nhập thông tin tài xế thứ " + (i+1));
            Driver driver = new Driver();
            driver.inputNewData();
            insertNewDriver(driver);
            MainRun.DRIVERS.add(driver);
        }
    }

    public static void showDriver() {
        MainRun.DRIVERS.forEach(System.out::println);
    }
}