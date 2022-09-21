package com.training.data_storage.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface DataStorageService {

    Object getObjectFromQueryString(String queryString) throws ClassNotFoundException, InvocationTargetException, InstantiationException, NoSuchMethodException, IllegalAccessException;

    List<Object> getObjectsFromQueryString(String queryString) throws ClassNotFoundException, InvocationTargetException, InstantiationException, NoSuchMethodException, IllegalAccessException;
}
