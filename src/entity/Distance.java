package entity;

import java.io.Serializable;

public class Distance implements Serializable {
    private int driverId;
    private String driverName;
    private float distanceTotal;

    public Distance() {
    }

    public Distance(int driverId, String driverName, float distanceTotal) {
        this.driverId = driverId;
        this.driverName = driverName;
        this.distanceTotal = distanceTotal;
    }

    @Override
    public String toString() {
        return "Distance{" +
                "driverId=" + driverId +
                ", driverName='" + driverName + '\'' +
                ", distanceTotal=" + distanceTotal +
                '}';
    }
}
