package entity;

import Database.BusRouteTable;
import Database.DriverTable;
import Database.ScheduleManagementTable;
import Main.MainRun;
import util.CheckStyle;

import java.io.Serializable;
import java.util.InputMismatchException;
import java.util.Scanner;

import static util.CheckStyle.inputCheck;

public class ScheduleManagement implements AutoIdIncreasable, NewDataCreatable, Serializable {
    private static int AUTO_ID = 1;

    private int id;
    private int driverId;
    private int busRouteId;
    private int turnNumber;

    public ScheduleManagement() {
        this.increaseId();
    }

    public ScheduleManagement(int driverId, int busRouteId, int routeNumber) {
        if(MainRun.SCHEDULE_MANAGEMENTS == null){
            this.id = AUTO_ID++;
        }else {
            int index = MainRun.SCHEDULE_MANAGEMENTS.size()-1;
            int valueUp = MainRun.SCHEDULE_MANAGEMENTS.get(index).getId() + 1;
            this.id = valueUp++;
        }
        this.driverId = driverId;
        this.busRouteId = busRouteId;
        this.turnNumber = routeNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public int getBusRouteId() {
        return busRouteId;
    }

    public void setBusRouteId(int busRouteId) {
        this.busRouteId = busRouteId;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    @Override
    public String toString() {
        return "ScheduleManagement{" +
                "id=" + id +
                ", driverId=" + driverId +
                ", busRouteId=" + busRouteId +
                ", routeNumber=" + turnNumber +
                '}';
    }

    @Override
    public void increaseId() {
        this.id = AUTO_ID++;
    }

    @Override
    public void inputNewData() {
        System.out.print("Nhập id của tài xế: ");
        int driver_id = 0;
        do {
            driver_id = inputCheck();
            if (DriverTable.findById(driver_id) != null) break;
            System.out.print("Bạn cần nhập lại số mã tài đã tồn tại: ");
        } while (true);
        this.setDriverId(driver_id);
        System.out.print("Nhập mã của tuyến: ");
        int busRoute_id = 0;
        do {
            busRoute_id = inputCheck();
            if (BusRouteTable.findById(busRoute_id) != null &&
                    ScheduleManagementTable.checkUnique(driver_id,busRoute_id) == null) break;
            System.out.print("Bạn cần nhập lại số mã tuyến đã tồn tại, và không bị trùng: ");
        } while (true);
        this.setBusRouteId(busRoute_id);
        System.out.print("Nhập số lượt chạy của tài xế ở tuyến này: ");
        int turn = 0;
        do {
            turn = inputCheck();
            if (ScheduleManagementTable.checkLimitTurn(turn,driver_id) <= 15) break;
            System.out.print("Số lượt chạy đã vượt quá 15 lượt. Xin vui lòng nhập lại: ");
        } while (true);
        this.setTurnNumber(turn);
    }
}
