package com.safe.storage.daos;

import com.safe.storage.common.Login;
import com.safe.storage.database.DBConnection;
import com.safe.storage.models.GenericItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GenericItemDao implements Dao<GenericItem> {

    @Override
    public Optional<GenericItem> create(GenericItem newItem) {
        String sql = "INSERT INTO generic_items (userId, name, value, createdDate, data, description, price) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (
            Connection connection = DBConnection.connect();
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            pstmt.setInt(1, Login.getUser().getId());
            pstmt.setString(2, newItem.getName());
            pstmt.setFloat(3, newItem.getValue());
            pstmt.setDate(4, new java.sql.Date(newItem.getCreatedDate().getTime()));
            pstmt.setBytes(5, newItem.getData());
            pstmt.setString(6, newItem.getDescription());
            pstmt.setDouble(7, newItem.getPrice());
            pstmt.executeUpdate();

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                newItem.setId(generatedKeys.getInt(1));
                return Optional.of(newItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<GenericItem> read(Object id) {
        String sql = "SELECT * FROM generic_items WHERE id = ? AND userId = ?";
        try (
            Connection connection = DBConnection.connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {
            pstmt.setObject(1, id);
            pstmt.setObject(2, Login.getUser().getId());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                GenericItem item = new GenericItem();
                item.setId(rs.getInt("id"));
                item.setUserId(rs.getInt("userId"));
                item.setName(rs.getString("name"));
                item.setValue(rs.getFloat("value"));
                item.setCreatedDate(rs.getDate("createdDate"));
                item.setData(rs.getBytes("data"));
                item.setDescription(rs.getString("description"));
                item.setPrice(rs.getDouble("price"));
                return Optional.of(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean update(GenericItem item) {
        String sql = "UPDATE generic_items SET name = ?, value = ?, createdDate = ?, data = ?, description = ?, price = ? WHERE id = ? AND userId = ?";
        try (
            Connection connection = DBConnection.connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {
            pstmt.setString(1, item.getName());
            pstmt.setFloat(2, item.getValue());
            pstmt.setDate(3, new java.sql.Date(item.getCreatedDate().getTime()));
            pstmt.setBytes(4, item.getData());
            pstmt.setString(5, item.getDescription());
            pstmt.setDouble(6, item.getPrice());
            pstmt.setInt(7, item.getId());
            pstmt.setInt(8, Login.getUser().getId());
            return pstmt.executeUpdate() > 0; // Returns true if at least one row was updated
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Object id) {
        String sql = "DELETE FROM generic_items WHERE id = ? AND userId = ?";
        try (
            Connection connection = DBConnection.connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {
            pstmt.setObject(1, id);
            pstmt.setObject(2, Login.getUser().getId());
            return pstmt.executeUpdate() > 0; // Returns true if at least one row was deleted
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<GenericItem> getAll() {
        List<GenericItem> items = new ArrayList<>();
        String sql = "SELECT * FROM generic_items WHERE userId = ?";
        try (
            Connection connection = DBConnection.connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {
            pstmt.setInt(1, Login.getUser().getId());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                GenericItem item = new GenericItem();
                item.setId(rs.getInt("id"));
                item.setUserId(rs.getInt("userId"));
                item.setName(rs.getString("name"));
                item.setValue(rs.getFloat("value"));
                item.setCreatedDate(rs.getDate("createdDate"));
                item.setData(rs.getBytes("data"));
                item.setDescription(rs.getString("description"));
                item.setPrice(rs.getDouble("price"));
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    @Override
    public List<GenericItem> searchBy(String key, Object value) {
        List<GenericItem> items = new ArrayList<>();
        String sql = "SELECT * FROM generic_items WHERE " + key + " = ? AND userId = ?";
        try (
            Connection connection = DBConnection.connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {
            pstmt.setObject(1, value);
            pstmt.setObject(2, Login.getUser().getId());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                GenericItem item = new GenericItem();
                item.setId(rs.getInt("id"));
                item.setUserId(rs.getInt("userId"));
                item.setName(rs.getString("name"));
                item.setValue(rs.getFloat("value"));
                item.setCreatedDate(rs.getDate("createdDate"));
                item.setData(rs.getBytes("data"));
                item.setDescription(rs.getString("description"));
                item.setPrice(rs.getDouble("price"));
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    @Override
    public boolean setUp() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS generic_items ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "userId INTEGER NOT NULL,"
                + "name TEXT NOT NULL,"
                + "value REAL NOT NULL,"
                + "createdDate DATE NOT NULL,"
                + "data BLOB,"
                + "description TEXT,"
                + "price REAL NOT NULL"
                + ");";
        try (
            Connection connection = DBConnection.connect();
            Statement stmt = connection.createStatement()
        ) {
            stmt.execute(createTableSQL);
            return true; // Table created or already exists
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Failed to create table
        }
    }

    @Override
    public boolean purgeDB() {
        String dropTableSQL = "DROP TABLE IF EXISTS generic_items;";
        try (
            Connection connection = DBConnection.connect();
            Statement stmt = connection.createStatement()
        ) {
            stmt.execute(dropTableSQL);
            return true; // Table dropped
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Failed to drop table
        }
    }
}
