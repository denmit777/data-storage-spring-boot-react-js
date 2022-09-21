package com.training.data_storage.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.training.data_storage.dao.ObjectDAO;
import com.training.data_storage.service.DataStorageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class DataStorageServiceImpl implements DataStorageService {

    private final ObjectDAO objectDAO;

    @Override
    public Object getObjectFromQueryString(String queryString) throws ClassNotFoundException, InvocationTargetException, InstantiationException, NoSuchMethodException, IllegalAccessException {

        String objectString = objectDAO.query(queryString);

        JsonReader reader = new JsonReader(new StringReader(objectString));
        reader.setLenient(true);

        return new Gson().fromJson(reader, Object.class);
    }

    @Override
    public List<Object> getObjectsFromQueryString(String queryString) throws ClassNotFoundException, InvocationTargetException, InstantiationException, NoSuchMethodException, IllegalAccessException {
        String objectsString = objectDAO.query(queryString);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Object[] objectsArr = gson.fromJson(objectsString, Object[].class);

        return Arrays.asList(objectsArr);
    }
}
