package Main;

import Database.DriverTable;
import Database.BusRouteTable;
import Database.ScheduleManagementTable;
import entity.BusRoute;
import entity.Driver;
import entity.ScheduleManagement;
import util.DatabaseConnection;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MainRun {
    public static List<Driver> DRIVERS;
    public static List<BusRoute> BUS_ROUTES;
    public static List<ScheduleManagement> SCHEDULE_MANAGEMENTS;

    public static void main(String[] args) {
        initData();
        menu();
    }

    public static void initData(){
        DriverTable.createTable();
        BusRouteTable.createTable();
        ScheduleManagementTable.createTable();
        DRIVERS = DriverTable.getDrivers();
        BUS_ROUTES = BusRouteTable.getBusRoutes();
        SCHEDULE_MANAGEMENTS = ScheduleManagementTable.getScheduleManagements();
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
                    BusRouteTable.createNewBusRoute();
                    break;
                case 4:
                    BusRouteTable.showBusRoute();
                    break;
                case 5:
                    ScheduleManagementTable.createNewSchedule();
                    break;
                case 6:
                    ScheduleManagementTable.showSchedule();
                    break;
                case 7:
                    ScheduleManagementTable.sortRouteManagerment();
                    break;
                case 8:
                    ScheduleManagementTable.calculateTotalRound();
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
