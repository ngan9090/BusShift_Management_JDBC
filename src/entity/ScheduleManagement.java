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
        System.out.print("Nh???p id c???a t??i x???: ");
        int driver_id = 0;
        do {
            driver_id = inputCheck();
            if (DriverTable.findById(driver_id) != null) break;
            System.out.print("B???n c???n nh???p l???i s??? m?? t??i ???? t???n t???i: ");
        } while (true);
        this.setDriverId(driver_id);
        System.out.print("Nh???p m?? c???a tuy???n: ");
        int busRoute_id = 0;
        do {
            busRoute_id = inputCheck();
            if (BusRouteTable.findById(busRoute_id) != null &&
                    ScheduleManagementTable.checkUnique(driver_id,busRoute_id) == null) break;
            System.out.print("B???n c???n nh???p l???i s??? m?? tuy???n ???? t???n t???i, v?? kh??ng b??? tr??ng: ");
        } while (true);
        this.setBusRouteId(busRoute_id);
        System.out.print("Nh???p s??? l?????t ch???y c???a t??i x??? ??? tuy???n n??y: ");
        int turn = 0;
        do {
            turn = inputCheck();
            if (ScheduleManagementTable.checkLimitTurn(turn,driver_id) <= 15) break;
            System.out.print("S??? l?????t ch???y ???? v?????t qu?? 15 l?????t. Xin vui l??ng nh???p l???i: ");
        } while (true);
        this.setTurnNumber(turn);
    }
}
