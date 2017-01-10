package com.example.mathiasloh.bodyacceleration;

import java.util.ArrayList;

/**
 * Created by mathiasloh on 10/1/17.
 */

public class AccelData {
    private ArrayList<Long> timestamp;
    private ArrayList<Double> x;
    private ArrayList<Double> y;
    private ArrayList<Double> z;

    public AccelData(long timestamp, double x, double y, double z) {
        this.timestamp = timestamp;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
    public double getZ() {
        return z;
    }
    public void setZ(double z) {
        this.z = z;
    }

    public String toString()
    {
        return "t="+timestamp+", x="+x+", y="+y+", z="+z;
    }




}
