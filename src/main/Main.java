package main;

import helper.JDBB;


public class Main  {

    public static void main(String[] args) {
        JDBB.openConnection();
        // launch(args);
        JDBB.closeConnection();
    }
}