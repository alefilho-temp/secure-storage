package com.safe.storage.daos;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.safe.storage.common.Login;
import com.safe.storage.database.DBConnection;
import com.safe.storage.models.SafePassword;
import com.safe.storage.security.SafePasswordCode;

public class SafePasswordDao implements Dao<SafePassword> {
    
    // Method to save the image and return the new path
    public String saveImage(File imageFile) {
        System.out.println(imageFile.toPath());
        System.out.println(imageFile.getAbsolutePath());
        try {
            // Define the target directory (e.g., in the resources folder)
            String targetDirectory = "src/main/resources/images"; // Adjust this path as necessary
            File dir = new File(targetDirectory);

            // Create the directory if it doesn't exist
            if (!dir.exists()) {System.out.println("semdir");
                dir.mkdirs();
            }
            System.out.println("scomdir");
            // Define the new file path
            String newFileName = System.currentTimeMillis() + "_" + imageFile.getName(); // Unique name
            File newFile = new File(dir, newFileName);
            System.out.println("newFileName " + newFileName);
            System.out.println("newFile " + newFile);
            // Copy the file to the new location
            Files.copy(imageFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("asasdasd " );
            // Return the new file path (you can store this in the database)
            return newFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return "src/main/resources/placeholder.png";
        }
    }

    @Override
    public Optional<SafePassword> create(SafePassword newObject) {
        newObject = new SafePasswordCode().encode(newObject);

        String sql = "INSERT INTO safe_passwords (userId, imagePath, name, accessUrl, note, username, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (
            Connection connection = DBConnection.connect();
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            pstmt.setInt(1, Login.getUser().getId());
            pstmt.setString(2, saveImage(new File(newObject.getImagePath())));
            pstmt.setString(3, newObject.getName());
            pstmt.setString(4, newObject.getAccessUrl());
            pstmt.setString(5, newObject.getNote());
            pstmt.setString(6, newObject.getUsername());
            pstmt.setString(7, newObject.getPassword());
            pstmt.executeUpdate();

            newObject = new SafePasswordCode().decode(newObject);

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
    public Optional<SafePassword> read(Object id) {
        String sql = "SELECT * FROM safe_passwords WHERE id = ? AND userId = ?";
        try (
            Connection connection = DBConnection.connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {
            pstmt.setObject(1, id);
            pstmt.setInt(2, Login.getUser().getId());

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                SafePassword safePassword = new SafePassword();
                safePassword.setId(rs.getInt("id"));
                safePassword.setUserId(rs.getInt("userId")); // Set userId from the database
                safePassword.setImagePath(rs.getString("imagePath"));
                safePassword.setName(rs.getString("name"));
                safePassword.setAccessUrl(rs.getString("accessUrl"));
                safePassword.setNote(rs.getString("note"));
                safePassword.setUsername(rs.getString("username"));
                safePassword.setPassword(rs.getString("password"));

                safePassword = new SafePasswordCode().decode(safePassword);

                return Optional.of(safePassword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public boolean update(SafePassword newObject) {
        newObject = new SafePasswordCode().encode(newObject);

        String sql = "UPDATE safe_passwords SET imagePath = ?, name = ?, accessUrl = ?, note = ?, username = ?, password = ? WHERE id = ? AND userId = ?";
        try (
            Connection connection = DBConnection.connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {
            pstmt.setString(1, saveImage(new File(newObject.getImagePath())));
            pstmt.setString(2, newObject.getName());
            pstmt.setString(3, newObject.getAccessUrl());
            pstmt.setString(4, newObject.getNote());
            pstmt.setString(5, newObject.getUsername());
            pstmt.setString(6, newObject.getPassword());
            pstmt.setInt(7, newObject.getId());
            pstmt.setInt(8, Login.getUser().getId()); 

            return pstmt.executeUpdate() > 0; // Returns true if at least one row was updated
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean delete(Object id) {
        String sql = "DELETE FROM safe_passwords WHERE id = ? AND userId = ?";
        try (
            Connection connection = DBConnection.connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {
            pstmt.setObject(1, id);
            pstmt.setInt(2, Login.getUser().getId());
            return pstmt.executeUpdate() > 0; // Returns true if at least one row was deleted
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public List<SafePassword> getAll() {
        List<SafePassword> safePasswords = new ArrayList<>();
        String sql = "SELECT * FROM safe_passwords WHERE userId = ?";
        try (
            Connection connection = DBConnection.connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {
            pstmt.setInt(1, Login.getUser().getId());

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                SafePassword safePassword = new SafePassword();
                safePassword.setId(rs.getInt("id"));
                safePassword.setUserId(rs.getInt("userId")); // Set userId from the database
                safePassword.setImagePath(rs.getString("imagePath"));
                safePassword.setName(rs.getString("name"));
                safePassword.setAccessUrl(rs.getString("accessUrl"));
                safePassword.setNote(rs.getString("note"));
                safePassword.setUsername(rs.getString("username"));
                safePassword.setPassword(rs.getString("password"));

                safePassword = new SafePasswordCode().decode(safePassword);

                safePasswords.add(safePassword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return safePasswords;
    }

    @Override
    public List<SafePassword> searchBy(String key, Object value) {
        List<SafePassword> safePasswords = new ArrayList<>();
        String sql = "SELECT * FROM safe_passwords WHERE " + key + " = ? AND userId = ?";
        try (
            Connection connection = DBConnection.connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {
            pstmt.setObject(1, value);
            pstmt.setInt(2, Login.getUser().getId());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                SafePassword safePassword = new SafePassword();
                safePassword.setId(rs.getInt("id"));
                safePassword.setUserId(rs.getInt("userId")); // Set userId from the database
                safePassword.setImagePath(rs.getString("imagePath"));
                safePassword.setName(rs.getString("name"));
                safePassword.setAccessUrl(rs.getString("accessUrl"));
                safePassword.setNote(rs.getString("note"));
                safePassword.setUsername(rs.getString("username"));
                safePassword.setPassword(rs.getString("password"));

                safePassword = new SafePasswordCode().decode(safePassword);

                safePasswords.add(safePassword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return safePasswords;
    }

    @Override
    public boolean setUp() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS safe_passwords ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "userId INTEGER NOT NULL,"
                + "imagePath TEXT,"
                + "name TEXT NOT NULL,"
                + "accessUrl TEXT,"
                + "note TEXT,"
                + "username TEXT,"
                + "password TEXT NOT NULL"
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
        String dropTableSQL = "DROP TABLE IF EXISTS safe_passwords;";
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
