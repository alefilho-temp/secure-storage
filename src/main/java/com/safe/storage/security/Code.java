package com.safe.storage.security;

public interface Code<T> {
    public T encode(T object);
    public T decode(T object);
}
