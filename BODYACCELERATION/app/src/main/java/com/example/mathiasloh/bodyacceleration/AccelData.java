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

    public AccelData() {
        this.timestamp = new ArrayList<Long>();
        this.x = new ArrayList<Double>();
        this.y = new ArrayList<Double>();
        this.z = new ArrayList<Double>();
    }

    public ArrayList<Long> getTimestamp() {
        return timestamp;
    }

    public ArrayList<Double> getX() {
        return x;
    }

    public ArrayList<Double> getY() {
        return y;
    }

    public ArrayList<Double> getZ() {
        return z;
    }


    public void addTimestamp(long t) {
        timestamp.add(t);
    }

    public void addX(double x) {
        this.x.add(x);
    }

    public void addY(double y) {
        this.y.add(y);
    }

    public void addZ(double z) {
        this.z.add(z);
    }

    public String toString()
    {
        return "t="+timestamp+", x="+x+", y="+y+", z="+z;
    }




}
