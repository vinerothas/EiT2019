package main;

import javafx.scene.Node;
import javafx.scene.chart.LineChart;

import java.util.Set;

public class LinePainter {

    static public void paint(LineChart<Number,Number> lineChart){
        int index = 0;
        if(Main.showAcc)paintLine(lineChart,index++, "orange");
        if(Main.showUltr)paintLine(lineChart,index++, "purple");
        if(Main.showDitemp)paintLine(lineChart,index++, "blue");
        if(Main.showAntemp)paintLine(lineChart,index++, "lightblue");
        if(Main.showPir)paintLine(lineChart,index++, "red");
        paintLine(lineChart,index, "green");
    }

    static private void paintLine(LineChart<Number,Number> lineChart, int index, String color){
        Set<Node> nodes = lineChart.lookupAll(".series"+index);
        for (Node n : nodes) {
            StringBuilder style = new StringBuilder();
            style.append("-fx-stroke: "+color+";");
            n.setStyle(style.toString());
        }
    }

}
