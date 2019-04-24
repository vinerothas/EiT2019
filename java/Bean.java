package main;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class Bean {

    // time
    // acc ultr ditemp antemp pir

    ArrayList<Date> time = new ArrayList<>();
    ArrayList<Double> acc = new ArrayList<>();
    ArrayList<Double> ultr = new ArrayList<>();
    ArrayList<Double> ditemp = new ArrayList<>();
    ArrayList<Double> antemp = new ArrayList<>();
    ArrayList<Double> pir = new ArrayList<>();

    ArrayList<Presence> presence = new ArrayList<>();

    double accHigh = 0;
    double ultrHigh = 0;
    double ultrLow = Integer.MAX_VALUE;
    double ditempHigh = 0;
    double ditempLow = Double.MAX_VALUE;
    double antempHigh = 0;
    double antempLow = Double.MAX_VALUE;
    double pirHigh = 0;

    ArrayList<Double> accAvg = new ArrayList<>();
    ArrayList<Double> ultrAvg = new ArrayList<>();
    ArrayList<Double> ditempAvg = new ArrayList<>();
    ArrayList<Double> antempAvg = new ArrayList<>();
    ArrayList<Double> pirAvg = new ArrayList<>();

    ArrayList<Double> accMax = new ArrayList<>();
    ArrayList<Double> ultrMax = new ArrayList<>();
    ArrayList<Double> ditempMax = new ArrayList<>();
    ArrayList<Double> antempMax = new ArrayList<>();
    ArrayList<Double> pirMax = new ArrayList<>();

    void calculateAverage(ArrayList<Double> list, ArrayList<Double> avg, int length){
        Iterator<Double> it = list.iterator();
        double[] lastX = new double[length];
        for (int i = 0; i < length; i++) {
            lastX[i] = it.next();
        }
        avg.add(getAvg(lastX));
        while(it.hasNext()){
            for (int i = 0; i < lastX.length-1; i++) {
                lastX[i] = lastX[i+1];
            }
            double d = it.next();
            lastX[lastX.length-1] = d;
            avg.add(getAvg(lastX));
        }

    }

    void calculateMax(ArrayList<Double> list, ArrayList<Double> max, int length){
        Iterator<Double> it = list.iterator();
        double[] lastX = new double[length];
        for (int i = 0; i < length; i++) {
            lastX[i] = it.next();
        }
        max.add(getAvg(lastX));
        while(it.hasNext()){
            for (int i = 0; i < lastX.length-1; i++) {
                lastX[i] = lastX[i+1];
            }
            double d = it.next();
            lastX[lastX.length-1] = d;
            max.add(getMax(lastX));
        }

    }

    private double getAvg(double[] data){
        double avg = 0;
        for (int i = 0; i < data.length; i++) {
            avg+=data[i];
        }
        avg/=(double)data.length;
        return avg;
    }

    private double getMax(double[] data){
        double max = data[0];
        for (int i = 1; i < data.length; i++) {
            max = Double.max(max,data[i]);
        }
        return max;
    }

}
