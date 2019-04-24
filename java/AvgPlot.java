package main;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class AvgPlot {

    public static void plot(Stage stage, Bean bean, int avgLength) {
        stage.setTitle("Presence data");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time");
        yAxis.setLabel("Normalized values");
        //creating the chart
        final LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);

        lineChart.setTitle("Presence data");
        lineChart.setCreateSymbols(false);
        Double[] acc = bean.accAvg.toArray(new Double[0]);
        Double[] ultr = bean.ultrAvg.toArray(new Double[0]);
        Double[] ditemp = bean.ditempAvg.toArray(new Double[0]);
        Double[] antemp = bean.antempAvg.toArray(new Double[0]);
        Double[] pir = bean.pirAvg.toArray(new Double[0]);
        Date[] time = bean.time.toArray(new Date[0]);

        bean.accHigh = 0;
        bean.ultrHigh = 0;
        bean.ultrLow = Integer.MAX_VALUE;
        bean.ditempHigh = 0;
        bean.ditempLow = Double.MAX_VALUE;
        bean.antempHigh = 0;
        bean.antempLow = Double.MAX_VALUE;
        bean.pirHigh = 0;
        for (int i = 0; i < acc.length; i++) {
            if(acc[i]> bean.accHigh) bean.accHigh = acc[i];
            if(ultr[i]> bean.ultrHigh) bean.ultrHigh = ultr[i];
            if(ditemp[i]> bean.ditempHigh) bean.ditempHigh = ditemp[i];
            if(antemp[i]> bean.antempHigh) bean.antempHigh = antemp[i];
            if(pir[i]> bean.pirHigh) bean.pirHigh = pir[i];

            if(ultr[i] < bean.ultrLow) bean.ultrLow = ultr[i];
            if(ditemp[i] < bean.ditempLow) bean.ditempLow = ditemp[i];
            if(antemp[i] < bean.antempLow) bean.antempLow = antemp[i];
        }


        XYChart.Series series1 = new XYChart.Series();
        series1.setName("acc");
        XYChart.Series series2 = new XYChart.Series();
        series2.setName("ultr");
        XYChart.Series series3 = new XYChart.Series();
        series3.setName("ditemp");
        XYChart.Series series4 = new XYChart.Series();
        series4.setName("antemp");
        XYChart.Series series5 = new XYChart.Series();
        series5.setName("pir");
        XYChart.Series series6 = new XYChart.Series();
        series6.setName("presence");

        Iterator<Presence> it = null;
        Presence currentPresence = null;
        Presence nextPresence = null;
        boolean isPresent = false;
        if(!bean.presence.isEmpty()) {
            it = bean.presence.iterator();
            currentPresence = it.next();
            nextPresence = it.next();
            isPresent = currentPresence.present;
        }


        for (int i = 0; i < acc.length; i++) {
            series1.getData().add(new XYChart.Data(i, acc[i]/(double)bean.accHigh));
            series2.getData().add(new XYChart.Data(i, (ultr[i]-bean.ultrLow)/(double)(bean.ultrHigh-bean.ultrLow)));
            series3.getData().add(new XYChart.Data(i, (ditemp[i]-bean.ditempLow)/(bean.ditempHigh-bean.ditempLow)));
            series4.getData().add(new XYChart.Data(i, (antemp[i]-bean.antempLow)/(bean.antempHigh-bean.antempLow)));
            series5.getData().add(new XYChart.Data(i, pir[i]/(double)bean.pirHigh));

            if(!bean.presence.isEmpty()) {
                if (nextPresence.timestamp.before(time[i + avgLength - 1])) {
                    isPresent = nextPresence.present;
                    currentPresence = nextPresence;
                    if (it.hasNext()) nextPresence = it.next();
                }
                if (isPresent) series6.getData().add(new XYChart.Data(i, 1));
                else series6.getData().add(new XYChart.Data(i, 0));
            }

        }


        if(Main.showAcc)lineChart.getData().add(series1);
        if(Main.showUltr)lineChart.getData().add(series2);
        if(Main.showDitemp)lineChart.getData().add(series3);
        if(Main.showAntemp)lineChart.getData().add(series4);
        if(Main.showPir)lineChart.getData().add(series5);
        lineChart.getData().add(series6);

        Scene scene  = new Scene(lineChart,800,600);

        stage.setScene(scene);
        stage.show();

        LinePainter.paint(lineChart);
    }
}
