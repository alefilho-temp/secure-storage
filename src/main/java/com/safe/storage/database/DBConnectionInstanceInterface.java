package com.safe.storage.database;

import java.sql.Connection;

public interface DBConnectionInstanceInterface {
    public Connection connect();
    
    public Connection getConnection();

    public boolean disconnect();
}
