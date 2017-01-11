package com.example.mathiasloh.bodyacceleration;

import java.util.ArrayList;

/**
 * Created by Cecile on 10/01/2017.
 */

public class Calculations {

    /**
     * This function finds the maximum of norm and returns the index.
     * We can then calculate the max if we need it
     * @param accx
     * @param accy
     * @param timestamp
     * @return
     */
    static int calculateIndex(ArrayList<Double> accx, ArrayList<Double> accy, ArrayList<Double> timestamp){
        double max = 0.0;
        int index = -1;

        for (int i=0; i < timestamp.size();i++){
            if ((accx.get(i)*accx.get(i)) + (accy.get(i)*accy.get(i)) > max){
                max = (accx.get(i)*accx.get(i)) + (accy.get(i)*accy.get(i));
                index = i;
            }
        }
        return index;
    }

    static double calculateAngle(int i, ArrayList<Double> accx, ArrayList<Double> accy){
        return Math.toDegrees(Math.atan(accy.get(i)/accx.get(i)));
    }
}
