package main;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private final static int test = 1;
    final static boolean showAcc = false;
    final static boolean showUltr = false;
    final static boolean showDitemp = true;
    final static boolean showAntemp = false;
    final static boolean showPir = true;
    final static boolean showAvg = true;
    final static boolean showMax = false;
    final static int avgLength = 10;
    final static int maxLength = 10;

    @Override
    public void start(Stage primaryStage) {
        Reader reader = new Reader();
        Bean bean = new Bean();
        reader.readData(bean,test);

        if(showAvg) {
            bean.calculateAverage(bean.acc, bean.accAvg, avgLength);
            bean.calculateAverage(bean.ultr, bean.ultrAvg, avgLength);
            bean.calculateAverage(bean.ditemp, bean.ditempAvg, avgLength);
            bean.calculateAverage(bean.antemp, bean.antempAvg, avgLength);
            bean.calculateAverage(bean.pir, bean.pirAvg, avgLength);
        }
        if(showMax) {
            bean.calculateMax(bean.acc, bean.accMax, maxLength);
            bean.calculateMax(bean.ultr, bean.ultrMax, maxLength);
            bean.calculateMax(bean.ditemp, bean.ditempMax, maxLength);
            bean.calculateMax(bean.antemp, bean.antempMax, maxLength);
            bean.calculateMax(bean.pir, bean.pirMax, maxLength);
        }

        if(showAvg)AvgPlot.plot(primaryStage,bean, avgLength);
        else if(showMax)MaxPlot.plot(primaryStage,bean);
        else DataPlot.plot(primaryStage,bean);

        System.out.println();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
