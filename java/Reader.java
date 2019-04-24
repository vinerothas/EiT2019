package main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

public class Reader {

    // time
    // acc ultr ditemp antemp pir
    public void readData(Bean bean, int test) {
        String directory = "resources/data"+test;
        ArrayList<String> names = new ArrayList<>();
        String labelsPath = null;
        final File folder = new File(getClass().getResource(directory).getFile());
        for (final File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory()) {
                if (fileEntry.getName().startsWith("data")) {
                    names.add(fileEntry.getName());
                }else if(fileEntry.getName().startsWith("labels")){
                    labelsPath = directory+"/"+fileEntry.getName();
                }
            }
        }

        System.out.println("Data files found: " + names.size());

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Iterator<String> it = names.iterator();
        while (it.hasNext()) {
            String filename = directory + "/" + it.next();

            File file = new File(getClass().getResource(filename).getFile());

            // 03/24/2019, 11:27:28
            // 2 42 22.13 0.54 1
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] parts = line.split(" ");
                    Date date = format.parse(parts[0].substring(0, parts[0].length() - 1) + " " + parts[1]);
                    bean.time.add(date);

                    line = scanner.nextLine();
                    parts = line.split(" ");
                    double acc = Integer.parseInt(parts[0]);
                    double ultr = Integer.parseInt(parts[1]);
                    double ditemp = Double.parseDouble(parts[2]);
                    double antemp = Double.parseDouble(parts[3]);
                    double pir = Integer.parseInt(parts[4]);
                    bean.acc.add(acc);
                    bean.ultr.add(ultr);
                    bean.ditemp.add(ditemp);
                    bean.antemp.add(antemp);
                    bean.pir.add(pir);

                    if(acc> bean.accHigh) bean.accHigh = acc;
                    if(ultr> bean.ultrHigh) bean.ultrHigh = ultr;
                    if(ditemp> bean.ditempHigh) bean.ditempHigh = ditemp;
                    if(antemp> bean.antempHigh) bean.antempHigh = antemp;
                    if(pir> bean.pirHigh) bean.pirHigh = pir;

                    if(ultr < bean.ultrLow) bean.ultrLow = ultr;
                    if(ditemp < bean.ditempLow) bean.ditempLow = ditemp;
                    if(antemp < bean.antempLow) bean.antempLow = antemp;

                    scanner.nextLine();
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }

        if(labelsPath != null) {
            File file = new File(getClass().getResource(labelsPath).getFile());
            format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] parts = line.split(" ");
                    Presence presence = new Presence();
                    presence.timestamp = format.parse( parts[1] + " "+parts[2]);
                    int p = Integer.parseInt(parts[0]);
                    if(p ==1) presence.present = true;
                    else presence.present = false;
                    bean.presence.add(presence);
                }
            }catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }
}
