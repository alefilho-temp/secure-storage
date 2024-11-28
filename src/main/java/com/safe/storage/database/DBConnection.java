package com.safe.storage.database;

import java.sql.Connection;

public class DBConnection {
    protected static Connection connection;

    protected static DBConnectionInterface instance;
    public static void setInstance(DBConnectionInterface instance) {
        DBConnection.instance = instance;
        System.out.println("DataBase instance: " + instance.getClass().getName());
    }

    // public DBConnection(DBConnectionInterface newInstance) {
    //     instance = newInstance;
    // }

    public static Connection connect() {
        return instance.connect();
    };

    public static Connection getConnection() {
        return instance.getConnection();
    }

    public static boolean disconnect() {
        return instance.disconnect();
    }
}
