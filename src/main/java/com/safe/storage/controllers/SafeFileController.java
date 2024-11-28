package com.safe.storage.controllers;

import com.safe.storage.daos.SafeFileDao;
import com.safe.storage.models.SafeFile;
import com.safe.storage.security.SafeFileCode;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class SafeFileController implements Controller<SafeFile> {
    private SafeFileDao safeFileDao;
    private ObservableList<SafeFile> lista = FXCollections.observableArrayList();

    // Properties for SafeFile
    private SimpleStringProperty name = new SimpleStringProperty("");
    private SimpleStringProperty originalName = new SimpleStringProperty("");
    private SimpleStringProperty note = new SimpleStringProperty("");

    public SafeFileController() {
        this.safeFileDao = new SafeFileDao();
        loadAllFiles(); // Load all files initially
    }

    @Override
    public void save(SafeFile object) {
        if (object.getId() == -1) { // New object
            SafeFile createdFile = safeFileDao.create(object).orElse(null);
            if (createdFile != null) {
                lista.add(createdFile); // Add the new SafeFile to the list
            }
        } else { // Existing object
            boolean updated = safeFileDao.update(object);
            if (updated) {
                // Update the existing SafeFile in the list
                int index = findFileIndexById(object.getId());
                if (index != -1) {
                    // lista.set(index, object); // Update the object in the list
                    lista.set(index, new SafeFileCode().decode(object));
                }
            }
        }
    }

    @Override
    public void delete(SafeFile object) {
        boolean deleted = safeFileDao.delete(object.getId());
        if (deleted) {
            lista.remove(object); // Remove the SafeFile from the list
        }
    }

    @Override
    public ObservableList<SafeFile> findAll() {
        return lista;
    }

    @Override
    public ObservableList<SafeFile> searchBy(String key, Object value) {
        // lista.clear();
        // lista.addAll(safeFileDao.searchBy(key, value));
        // return lista;

        List<SafeFile> files = safeFileDao.getAll();

        lista.clear();

        for (SafeFile safeFile : files) {
            if (safeFile.getName().equals(value)) lista.add(safeFile);
        }

        return lista;
    }

    @Override
    public void clearFields() {
        name.set("");
        originalName.set("");
        note.set("");
    }

    @Override
    public void toView(SafeFile object) {
        if (object != null) {
            name.set(object.getName());
            originalName.set(object.getOriginalName());
            note.set(object.getNote());
        }
    }

    @Override
    public ObservableList<SafeFile> getObservableList() {
        return lista;
    }

    // Getter methods for properties
    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleStringProperty originalNameProperty() {
        return originalName;
    }

    public SimpleStringProperty noteProperty() {
        return note;
    }

    public void loadAllFiles() {
        lista.clear();
        lista.addAll(safeFileDao.getAll());
    }

    private int findFileIndexById(int id) {
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == id) {
                return i; // Return the index if found
            }
        }
        return -1; // Return -1 if not found
    }
}
