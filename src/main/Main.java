package main;

import helper.JDBC;


public class Main  {

    public static void main(String[] args) {
        JDBC.openConnection();
        // launch(args);
        JDBC.closeConnection();
    }
}