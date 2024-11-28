package com.safe.storage.daos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.safe.storage.database.DBConnection;
import com.safe.storage.models.User;

public class UserDao implements Dao<User> {

    @Override
    public Optional<User> create(User newObject) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (
            Connection connection = DBConnection.connect(); 
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            pstmt.setString(1, newObject.getUsername());
            pstmt.setString(2, newObject.getPassword());
            pstmt.executeUpdate();

            // Get the generated ID
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                newObject.setId(generatedKeys.getInt(1));

                return Optional.of(newObject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> read(Object id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (
            Connection connection = DBConnection.connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {
            pstmt.setObject(1, id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));

                return Optional.of(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return Optional.empty();
    }

    @Override
    public boolean update(User newObject) {
        String sql = "UPDATE users SET username = ?, password = ? WHERE id = ?";
        try (
            Connection connection = DBConnection.connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {
            pstmt.setString(1, newObject.getUsername());
            pstmt.setString(2, newObject.getPassword());
            pstmt.setInt(3, newObject.getId());

            return pstmt.executeUpdate() > 0; // Returns true if at least one row was updated
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    @Override
    public boolean delete(Object id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (
            Connection connection = DBConnection.connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {
            pstmt.setObject(1, id);

            return pstmt.executeUpdate() > 0; // Returns true if at least one row was deleted
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (
            Connection connection = DBConnection.connect();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql)
        ) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return users;
    }

    @Override
    public List<User> searchBy(String key, Object value) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE " + key + " = ?";
        try (
            Connection connection = DBConnection.connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {
            pstmt.setObject(1, value);

            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public boolean setUp() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "username TEXT NOT NULL UNIQUE,"
                + "password TEXT NOT NULL"
                + ");";
        try (
            Connection connection = DBConnection.connect ();
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
        String dropTableSQL = "DROP TABLE IF EXISTS users;";
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
