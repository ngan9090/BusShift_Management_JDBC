package Main;

import Database.DriverTable;
import constant.Database;
import entity.Driver;
import util.DatabaseConnection;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MainRun {
    public static List<Driver> DRIVERS;

    public static void main(String[] args) {
        initData();
        menu();

    }

    public static void initData(){
        Connection connection = DatabaseConnection.openConnection(Database.DRIVER_STRING, Database.URL, Database.USERNAME, Database.PASSWORD);
        //Kiểm tra bảng có tồn tại hay không
        DatabaseMetaData dbm = null;
        try {
            dbm = connection.getMetaData();
            ResultSet tables = dbm.getTables(null, "SQL4", DriverTable.DRIVER_TABLE_NAME , null);
            if(!tables.next()) {
                try {
                    PreparedStatement pre = connection.prepareStatement("CREATE TABLE SQL4.Driver("
                            + "Ids number primary key, "
                            + "Names VARCHAR (20) NOT NULL, "
                            + "Address VARCHAR (100) NOT NULL, "
                            + "PhoneNumber VARCHAR (20) NOT NULL, "
                            + "Levels VARCHAR (20))");
                    pre.executeQuery();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        DRIVERS = DriverTable.getDrivers();

    }

    private static void menu() {
        do {
            int functionChoice = showMenu();
            switch (functionChoice) {
                case 1:
                    DriverTable.createNewDriver();
                    break;
                case 2:
                    DriverTable.showDriver();
                    break;
                case 3:
                    //BusRouteService.createNewBusRoute();
                    break;
                case 4:
                    //BusRouteService.showBusRoute();
                    break;
                case 5:
                    //DriverManagementService.createDrivingSchedule();
                    break;
                case 6:
                    //DriverManagementService.showData();
                    break;
                case 7:
                    //DriverManagementService.sortRouteManagerment();
                    break;
                case 8:
                    //DriverManagementService.calculateTotalRound();
                    break;
                case 9:
                    return;
            }
        } while (true);
    }

    private static int showMenu() {
        printMenu();
        int functionChoice = -1;
        do {
            try {
                functionChoice = new Scanner(System.in).nextInt();
            } catch (InputMismatchException ex) {
                System.out.print("Chức năng cần chọn là 1 số nguyên, yêu cầu nhập lại: ");
                continue;
            }
            if (functionChoice >= 1 && functionChoice <= 9) {
                break;
            }
            System.out.print("Chức năng vừa chọn không hợp lệ, vui lòng nhập lại: ");
        } while (true);
        return functionChoice;
    }

    private static void printMenu() {
        System.out.println("--------PHẦN MỀM QUẢN LÝ PHÂN CÔNG LÁT XE BUÝT------");
        System.out.println("1. Nhập danh sách lái xe mới.");
        System.out.println("2. In ra danh sách lái xe mới.");
        System.out.println("3. Nhập danh sách tuyến xe mới.");
        System.out.println("4. In ra danh sách tuyến xe mới.");
        System.out.println("5. Phân công lái xe cho các tài xế.");
        System.out.println("6. In ra danh sách đã phân công.");
        System.out.println("7. Sắp xếp danh sách phân công lái xe.");
        System.out.println("8. Lập bảng thống kê tổng khoảng cách chạy xe trong ngày của từng lái xe.");
        System.out.println("9. Thoát");
        System.out.print(" Xin mời chọn chức năng: ");
    }

}
