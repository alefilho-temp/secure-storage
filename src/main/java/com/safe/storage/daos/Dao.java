package com.safe.storage.daos;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    Optional<T> create(T newObject);
    Optional<T> read(Object id);
    boolean update(T newObject);
    boolean delete(Object id);
    List<T> getAll();
    List<T> searchBy(String key, Object value);

    boolean setUp();
    boolean purgeDB();
}
