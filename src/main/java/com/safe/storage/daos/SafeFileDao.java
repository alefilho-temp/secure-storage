package com.safe.storage.daos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.safe.storage.common.Login;
import com.safe.storage.database.DBConnection;
import com.safe.storage.models.SafeFile;
import com.safe.storage.security.SafeFileCode;

public class SafeFileDao implements Dao<SafeFile> {

    @Override
    public Optional<SafeFile> create(SafeFile newObject) {
        newObject = new SafeFileCode().encode(newObject);

        String sql = "INSERT INTO safe_files (userId, name, originalName, note, data, size) VALUES (?, ?, ?, ?, ?, ?)";
        try (
            Connection connection = DBConnection.connect();
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            pstmt.setInt(1, Login.getUser().getId());
            pstmt.setString(2, newObject.getName());
            pstmt.setString(3, newObject.getOriginalName());
            pstmt.setString(4, newObject.getNote());
            pstmt.setBytes(5, newObject.getData());
            pstmt.setInt(6, newObject.getSize());
            pstmt.executeUpdate();

            newObject = new SafeFileCode().decode(newObject);

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
    public Optional<SafeFile> read(Object id) {
        String sql = "SELECT * FROM safe_files WHERE id = ? AND userId = ?";
        try (
            Connection connection = DBConnection.connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {
            pstmt.setObject(1, id);
            pstmt.setInt(2, Login.getUser().getId());

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                SafeFile safeFile = new SafeFile();
                safeFile.setId(rs.getInt("id"));
                safeFile.setUserId(rs.getInt("userId"));
                safeFile.setName(rs.getString("name"));
                safeFile.setOriginalName(rs.getString("originalName"));
                safeFile.setNote(rs.getString("note"));
                safeFile.setData(rs.getBytes("data"));
                safeFile.setSize(rs.getInt("size"));

                safeFile = new SafeFileCode().decode(safeFile);

                return Optional.of(safeFile);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public boolean update(SafeFile newObject) {
        newObject = new SafeFileCode().encode(newObject);

        String sql = "UPDATE safe_files SET name = ?, originalName = ?, note = ?, data = ?, size = ? WHERE id = ? AND userId = ?";
        try (
            Connection connection = DBConnection.connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {
            pstmt.setString(1, newObject.getName());
            pstmt.setString(2, newObject.getOriginalName());
            pstmt.setString(3, newObject.getNote());
            pstmt.setBytes(4, newObject.getData());
            pstmt.setInt(5, newObject.getSize());
            pstmt.setInt(6, newObject.getId());
            pstmt.setInt(7, Login.getUser().getId()); 

            return pstmt.executeUpdate() > 0; // Returns true if at least one row was updated
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean delete(Object id) {
        String sql = "DELETE FROM safe_files WHERE id = ? AND userId = ?";
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
    public List<SafeFile> getAll() {
        List<SafeFile> safeFiles = new ArrayList<>();
        String sql = "SELECT * FROM safe_files WHERE userId = ?";
        try (
            Connection connection = DBConnection.connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)
            ) {
            pstmt.setInt(1, Login.getUser().getId());

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                SafeFile safeFile = new SafeFile();
                safeFile.setId(rs.getInt("id"));
                safeFile.setUserId(rs.getInt("userId"));
                safeFile.setName(rs.getString("name"));
                safeFile.setOriginalName(rs.getString("originalName"));
                safeFile.setNote(rs.getString("note"));
                safeFile.setData(rs.getBytes("data"));
                safeFile.setSize(rs.getInt("size"));

                safeFile = new SafeFileCode().decode(safeFile);

                safeFiles .add(safeFile);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return safeFiles;
    }

    @Override
    public List<SafeFile> searchBy(String key, Object value) {
        List<SafeFile> safeFiles = new ArrayList<>();
        String sql = "SELECT * FROM safe_files WHERE " + key + " = ? AND userId = ?";
        try (
            Connection connection = DBConnection.connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {
            pstmt.setObject(1, value);
            pstmt.setInt(2, Login.getUser().getId());

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                SafeFile safeFile = new SafeFile();
                safeFile.setId(rs.getInt("id"));
                safeFile.setUserId(rs.getInt("userId"));
                safeFile.setName(rs.getString("name"));
                safeFile.setOriginalName(rs.getString("originalName"));
                safeFile.setNote(rs.getString("note"));
                safeFile.setData(rs.getBytes("data"));
                safeFile.setSize(rs.getInt("size"));

                safeFile = new SafeFileCode().decode(safeFile);

                safeFiles.add(safeFile);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return safeFiles;
    }

    @Override
    public boolean setUp() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS safe_files ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "userId INTEGER NOT NULL,"
                + "name TEXT NOT NULL,"
                + "originalName TEXT NOT NULL,"
                + "note TEXT,"
                + "data BLOB,"
                + "size INTEGER NOT NULL"
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
        String dropTableSQL = "DROP TABLE IF EXISTS safe_files;";
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
