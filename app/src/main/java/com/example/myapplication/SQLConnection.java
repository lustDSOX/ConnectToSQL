package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnection {
    private static final String LOG = "DEBUG";
    private static String ip = "ngknn.ru";
    private static String port = "1433";
    private static String classs = "net.sourceforge.jtds.jdbc.Driver";
    private static String db = "33П-КР-Ермолаев";
    private static String un = "33П";
    private static String password = "12357";

    @SuppressLint("NewApi")
    public static Connection connect() {
        Connection conn = null;
        String ConnURL = null;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName(classs);
            ConnURL = "jdbc:jtds:sqlserver://" + ip +":"+port+";"
                    + "databaseName=" + db + ";user=" + un + ";password="
                    + password + ";";
            conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException e) {
            Log.d(LOG, e.getMessage());
        } catch (ClassNotFoundException e) {
            Log.d(LOG, e.getMessage());
        }
        return conn;
    }
}
