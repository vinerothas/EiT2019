package main;

import java.util.Date;

public class Presence {
    /*
    A person is present if  last timestamp < current data timestamp < next timestamp and last.present is true
     */

    Date timestamp;
    boolean present;
}
