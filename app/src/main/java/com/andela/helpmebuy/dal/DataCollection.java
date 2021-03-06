package com.andela.helpmebuy.dal;


import java.util.List;

public interface DataCollection<T> {

    void save(T data, DataCallback<T> callback);

    void get(String id, DataCallback<T> callback);

    void getAll(DataCallback<List<T>> callback);

    void query(String path, String arg, DataCallback<List<T>> callback);

    void query(String path, int arg, DataCallback<List<T>> callback);
}
