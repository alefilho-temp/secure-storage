package com.safe.storage.controllers;

import java.util.List;

import com.safe.storage.daos.SafePasswordDao;
import com.safe.storage.models.SafePassword;
import com.safe.storage.security.SafePasswordCode;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SafePasswordController implements Controller<SafePassword> {
    private SafePasswordDao safePasswordDao;
    private ObservableList <SafePassword> lista = FXCollections.observableArrayList();

    // Properties for SafePassword
    private IntegerProperty id = new SimpleIntegerProperty(-1);
    private StringProperty name = new SimpleStringProperty("");
    private StringProperty username = new SimpleStringProperty("");
    private StringProperty password = new SimpleStringProperty("");
    private StringProperty accessUrl = new SimpleStringProperty("");
    private StringProperty note = new SimpleStringProperty("");
    private StringProperty imagePath = new SimpleStringProperty(""); // Added imagePath property

    public SafePasswordController() {
        this.safePasswordDao = new SafePasswordDao();
        loadAllPasswords(); // Load all passwords initially
    }

    @Override
    public void save(SafePassword object) {
        System.out.println("saving");
        if (object.getId() == -1) { // New object
            SafePassword createdPassword = safePasswordDao.create(object).orElse(null);
            if (createdPassword != null) {
                lista.add(createdPassword); // Add the new SafePassword to the list
            }
        } else { // Existing object
            boolean updated = safePasswordDao.update(object);
            if (updated) {
                // Update the existing SafePassword in the list
                int index = findPasswordIndexById(object.getId());
                if (index != -1) {
                    lista.set(index, new SafePasswordCode().decode(object)); // Update the object in the list
                }
            }
        }
    }

    @Override
    public void delete(SafePassword object) {
        boolean deleted = safePasswordDao.delete(object.getId());
        if (deleted) {
            lista.remove(object); // Remove the SafePassword from the list
        }
    }

    @Override
    public ObservableList<SafePassword> findAll() {
        return lista;
    }

    @Override
    public ObservableList<SafePassword> searchBy(String key, Object value) {
        // lista.clear();
        // lista.addAll(safePasswordDao.searchBy(key, value));
        // return lista;

        List<SafePassword> passwords = safePasswordDao.getAll();

        lista.clear();

        for (SafePassword safePassword : passwords) {
            if (safePassword.getName().equals(value)) lista.add(safePassword);
        }

        return lista;
    }

    @Override
    public void clearFields() {
        id.set(-1);
        name.set("");
        username.set("");
        password.set("");
        accessUrl.set("");
        note.set("");
        imagePath.set(""); // Clear imagePath
    }

    @Override
    public void toView(SafePassword object) {
        if (object != null) {
            id.set(object.getId());
            name.set(object.getName());
            username.set(object.getUsername());
            password.set(object.getPassword());
            accessUrl.set(object.getAccessUrl());
            note.set(object.getNote());
            imagePath.set(object.getImagePath()); // Set imagePath
        }
    }

    @Override
    public ObservableList<SafePassword> getObservableList() {
        return lista;
    }

    // Additional getter methods for properties
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public StringProperty accessUrlProperty() {
        return accessUrl;
    }

    public StringProperty noteProperty() {
        return note;
    }

    public StringProperty imagePathProperty() {
        return imagePath; // Getter for imagePath property
    }

    public void loadAllPasswords() {
        lista.clear();
        lista.addAll(safePasswordDao.getAll());
    }

    private int findPasswordIndexById(int id) {
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == id) {
                return i; // Return the index if found
            }
        }
        return -1; // Return -1 if not found
    }
}
