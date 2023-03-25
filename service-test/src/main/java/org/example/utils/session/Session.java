package org.example.utils.session;

public interface Session {
    void put(SessionKey key, Object object);

    <T> T get(SessionKey key, Class<T> asType);

    Object get(SessionKey key);

    void checkIfExist(SessionKey key);

    void clear();
}
