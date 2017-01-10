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
        this.timestamp =new ArrayList<Long>();
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

    public void setTimestamp(ArrayList<Long> timestamp) {
        this.timestamp = timestamp;
    }

    public void setX(ArrayList<Double> x) {
        this.x = x;
    }

    public void setY(ArrayList<Double> y) {
        this.y = y;
    }

    public void setZ(ArrayList<Double> z) {
        this.z = z;
    }

    public void addTimestamp(long t) {
        ArrayList<Long> old = this.getTimestamp();
        old.add(t);
        this.setTimestamp(old);
    }

    public void addX(double x) {
        ArrayList<Double> old = this.getX();
        old.add(x);
        this.setX(old);
    }

    public void addY(double y) {
        ArrayList<Double> old = this.getY();
        old.add(y);
        this.setX(old);
    }

    public void addZ(double z) {
        ArrayList<Double> old = this.getZ();
        old.add(z);
        this.setX(old);
    }

    public String toString()
    {
        return "t="+timestamp+", x="+x+", y="+y+", z="+z;
    }




}
