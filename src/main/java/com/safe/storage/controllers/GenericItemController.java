package com.safe.storage.controllers;

import com.safe.storage.daos.GenericItemDao;
import com.safe.storage.models.GenericItem;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class GenericItemController implements Controller<GenericItem> {
    private GenericItemDao genericItemDao;
    private ObservableList<GenericItem> itemList = FXCollections.observableArrayList();

    // Properties for GenericItem
    private SimpleStringProperty name = new SimpleStringProperty("");
    private SimpleStringProperty description = new SimpleStringProperty("");
    private SimpleStringProperty value = new SimpleStringProperty("");
    private SimpleStringProperty price = new SimpleStringProperty("");

    public GenericItemController() {
        this.genericItemDao = new GenericItemDao();
        loadAllItems(); // Load all items initially
    }

    @Override
    public void save(GenericItem item) {
        if (item.getId() == -1) { // New item
            GenericItem createdItem = genericItemDao.create(item).orElse(null);
            if (createdItem != null) {
                itemList.add(createdItem); // Add the new GenericItem to the list
            }
        } else { // Existing item
            boolean updated = genericItemDao.update(item);
            if (updated) {
                // Update the existing GenericItem in the list
                int index = findItemIndexById(item.getId());
                if (index != -1) {
                    itemList.set(index, item);
                }
            }
        }
    }

    @Override
    public void delete(GenericItem item) {
        boolean deleted = genericItemDao.delete(item.getId());
        if (deleted) {
            itemList.remove(item); // Remove the GenericItem from the list
        }
    }

    @Override
    public ObservableList<GenericItem> findAll() {
        return itemList;
    }

    @Override
    public ObservableList<GenericItem> searchBy(String key, Object value) {
        List<GenericItem> items = genericItemDao.searchBy(key, value);
        itemList.clear();
        itemList.addAll(items);
        return itemList;
    }

    @Override
    public void clearFields() {
        name.set("");
        description.set("");
        value.set("");
        price.set("");
    }

    @Override
    public void toView(GenericItem item) {
        if (item != null) {
            name.set(item.getName());
            description.set(item.getDescription());
            value.set(String.valueOf(item.getValue()));
            price.set(String.valueOf(item.getPrice()));
        }
    }

    @Override
    public ObservableList<GenericItem> getObservableList() {
        return itemList;
    }

    // Getter methods for properties
    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public SimpleStringProperty valueProperty() {
        return value;
    }

    public SimpleStringProperty priceProperty() {
        return price;
    }

    public void loadAllItems() {
        itemList.clear();
        itemList.addAll(genericItemDao.getAll());
    }

    private int findItemIndexById(int id) {
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getId() == id) {
                return i; // Return the index if found
            }
        }
        return -1; // Return -1 if not found
    }
}
