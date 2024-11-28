package com.safe.storage.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLiteConnection extends DBConnectionInterface {
    public Connection connect() {
        try {
            return DriverManager.getConnection("jdbc:sqlite:database.db");
        } catch (Exception e) {
            System.err.println("Erro ao se conectar ao banco de dados SQLite: " + e.getMessage());
            e.printStackTrace();

            return null;
        }
    };
}
