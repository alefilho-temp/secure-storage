package com.safe.storage.controllers;

import javafx.collections.ObservableList;

public interface Controller<T> {
    void save(T object);
    void delete(T object);
    ObservableList<T> findAll();
    ObservableList<T> searchBy(String key, Object value);
    void clearFields();
    void toView(T object);
    
    ObservableList<T> getObservableList();
}
