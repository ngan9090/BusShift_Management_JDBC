package entity;

import Main.MainRun;

import java.io.Serializable;
import java.util.InputMismatchException;
import java.util.Scanner;

public class BusRoute implements AutoIdIncreasable, NewDataCreatable, Serializable {
    private static int AUTO_ID = 100;

    private int id;
    private float distance;
    private int stopStationNumber;

    public BusRoute() {
        this.increaseId();
    }

    public BusRoute(float distance, int stopStationNumber) {
        this.distance = distance;
        this.stopStationNumber = stopStationNumber;
        if(MainRun.BUS_ROUTES == null){
            this.id = AUTO_ID++;
        }else {
            int index = MainRun.BUS_ROUTES.size()-1;
            int valueUp = MainRun.BUS_ROUTES.get(index).getId() + 1;
            this.id = valueUp++;
        }
    }

    @Override
    public final void increaseId() {
        this.id = AUTO_ID++;
    }

    public int getId() {
        return id;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public int getStopStationNumber() {
        return stopStationNumber;
    }

    public void setStopStationNumber(int stopStationNumber) {
        this.stopStationNumber = stopStationNumber;
    }

    @Override
    public String toString() {
        return "BusRoute{" +
                "id=" + id +
                ", distance=" + distance +
                ", stopStationNumber=" + stopStationNumber +
                '}';
    }

    @Override
    public void inputNewData() {
        System.out.print("Nhập khoảng cách của tuyến xe: ");
        float distance = 0;
        do {
            try {
                distance = new Scanner(System.in).nextFloat();
            } catch (InputMismatchException ex) {
                System.out.print("Khoảng cách của phải là số nguyên, yêu cầu nhập lại: ");
                continue;
            }
            if (distance > 0) {
                this.setDistance(distance);
                break;
            }
            System.out.print("Khoảng cách của tuyến mới KHÔNG được là số âm, yêu cầu nhập lại: ");
        } while (true);

        System.out.print("Nhập số điểm dừng của tuyến xe: ");
        int stopStationNumber = 0;
        do {
            try {
                stopStationNumber = new Scanner(System.in).nextInt();
            } catch (InputMismatchException ex) {
                System.out.print("Số điểm dừng của phải là số nguyên, yêu cầu nhập lại: ");
                continue;
            }
            if (stopStationNumber > 0) {
                this.setStopStationNumber(stopStationNumber);
                break;
            }
            System.out.print("Số điểm dừng của tuyến mới KHÔNG được là số âm, yêu cầu nhập lại: ");
        } while (true);
    }
}
