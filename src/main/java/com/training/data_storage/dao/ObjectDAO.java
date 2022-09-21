package com.training.data_storage.dao;

import java.lang.reflect.InvocationTargetException;

public interface ObjectDAO {

    String query(String queryString) throws ClassNotFoundException, InvocationTargetException, InstantiationException, NoSuchMethodException, IllegalAccessException;
}
