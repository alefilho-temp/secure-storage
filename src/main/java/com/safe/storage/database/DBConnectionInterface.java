package com.safe.storage.database;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DBConnectionInterface implements DBConnectionInstanceInterface {
    protected static Connection connection;

    public Connection getConnection() {
        try {
            if (connection != null) {
                if (!connection.isClosed()) {
                    return connection;
                }
            }

            connection = connect();

            if (connection != null) {
                return connection;
            }

            return null;
        } catch (Exception e) {
            System.err.println("Erro ao se conectar ao banco de dados: " + e.getMessage());
            e.printStackTrace();

            return null;
        }
    }

    public boolean disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null; 
                return true; 
            } catch (SQLException e) {
                e.printStackTrace(); 
                return false;
            }
        }

        return false;
    }
}
