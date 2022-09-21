package com.training.data_storage.dao.impl;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.training.data_storage.dao.ObjectDAO;
import com.training.data_storage.exception.ObjectNotFoundException;
import com.training.data_storage.exception.WrongQueryException;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@Setter
@Getter
public class ObjectDAOImpl implements ObjectDAO {

    private static final Logger LOGGER = LogManager.getLogger(ObjectDAOImpl.class.getName());

    private static final String QUERY_CREATE_TABLE = "CREATE TABLE";
    private static final String QUERY_INSERT_INTO = "INSERT INTO";
    private static final String QUERY_SELECT_FROM = "SELECT";
    private static final String QUERY_SELECT_FROM_CITY_BY_ID = "WHERE id =";
    private static final String QUERY_UPDATE = "UPDATE";
    private static final String QUERY_DELETE_FROM = "DELETE FROM";
    private static final String PATH_TO_CLASS = "com.training.data_storage.model.";
    private static final String EMPTY_VALUE = "";
    private static final String REGEX_SPACE = " ";
    private static final String REGEX_COMMA_AND_SPACE = ", ";
    private static final String REGEX_NOT_NUMBERS = "[^\\d]";

    private List<Object> objects;

    private ObjectDAOImpl() {
        objects = new ArrayList<>();
    }

    @Override
    public String query(String queryString) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (isContainsString(queryString, QUERY_CREATE_TABLE)) {
            return createTable(queryString);
        }
        if (isContainsString(queryString, QUERY_INSERT_INTO)) {
            return save(queryString);
        }
        if (isContainsString(queryString, QUERY_SELECT_FROM)) {
            if (isContainsString(queryString, QUERY_SELECT_FROM_CITY_BY_ID)) {
                return getById(queryString).toString();
            } else {
                return getAll(queryString);
            }
        }
        if (isContainsString(queryString, QUERY_UPDATE)) {
            return update(queryString);
        }
        if (isContainsString(queryString, QUERY_DELETE_FROM)) {
            return deleteById(queryString);
        } else {
            throw new WrongQueryException("Wrong query");
        }
    }

    private String createTable(String queryString) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class clazz = getClass(queryString);

        Constructor con = clazz.getConstructor();

        Object obj = con.newInstance();

        LOGGER.info("New table: {}", obj);

        return obj.toString();
    }

    private String save(String queryString) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String newId = String.valueOf(objects.size() + 1);

        int lastIndexOfBrackets = queryString.lastIndexOf("(");

        String valuesString = newId + REGEX_COMMA_AND_SPACE +
                StringUtils.substringBetween(queryString.substring(lastIndexOfBrackets), "(", ")");

        String[] valuesArr = valuesString.split(REGEX_COMMA_AND_SPACE);

        int valuesCount = valuesArr.length;

        Class<?> clazz = getClass(queryString);

        Class[] parameters = new Class[valuesCount];
        int j = 0;

        for (String value : valuesArr) {
            parameters[j] = value.getClass();
            j++;
        }

        Object obj = clazz.getDeclaredConstructor(parameters).newInstance(valuesArr);
        LOGGER.info("New object: {}", obj);

        objects.add(obj);

        return obj.toString();
    }

    private String getAll(String queryString) {
        String searchParams = StringUtils.substringBetween(queryString.toLowerCase(), "SELECT".toLowerCase() + REGEX_SPACE,
                REGEX_SPACE + "FROM".toLowerCase());

        String searchValues = getSearchValues(queryString);

        if (searchParams.contains("*") && isContainsString(queryString, "WHERE")) {
            List<Object> objectsByValues = getObjectsByValues(searchValues);

            LOGGER.info("Select from all objects by values: {}", objectsByValues);

            return objectsByValues.toString();
        }
        if (searchParams.contains("*") && !isContainsString(queryString, "WHERE")) {
            LOGGER.info("Select from all objects: {}", objects);

            return objects.toString();
        } else {
            List<Object> objectsWithParamsByValues = getObjectsWithParamsByValues(searchParams, searchValues);

            LOGGER.info("Select from all objects with params by values: {}", objectsWithParamsByValues);

            return objectsWithParamsByValues.toString();
        }
    }

    private Object getById(String queryString) {
        String id = getIdFromQuery(queryString);

        return objects.stream()
                .filter(object -> object.toString().contains("\"id\": \"" + id) || object.toString().contains("id=" + id))
                .findAny()
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Object with id %s not found", id)));
    }

    private String update(String queryString) {
        String id = getIdFromQuery(queryString);
        Object obj = getById(queryString);

        List<String> oldValuesList = Stream.of(obj.toString().split(",")).collect(Collectors.toList());

        List<String> newValuesList = getNewValuesListForUpdate(queryString);

        for (int i = 0; i < oldValuesList.size(); i++) {
            for (String s : newValuesList) {
                if (oldValuesList.get(i).contains(StringUtils.substringBefore(s, ":"))) {
                    oldValuesList.set(i, s);
                }
            }
        }

        String updatedValuesString = oldValuesList.stream()
                .map(Objects::toString)
                .collect(Collectors.joining(",\n")) + "\n}";

        Object updatedObject = getObjectFromString(updatedValuesString);

        LOGGER.info("Updated object: {}", updatedObject);

        objects.set(Integer.parseInt(id) - 1, updatedObject);

        return updatedObject.toString();
    }

    private String deleteById(String queryString) {
        Object obj = getById(queryString);

        LOGGER.info("Deleted object : {}", obj);

        objects.remove(obj);

        LOGGER.info("All objects after deleted object: {}", objects);

        return objects.toString();
    }

    private Class getClass(String queryString) throws ClassNotFoundException {
        String[] queryArr = queryString.split(REGEX_SPACE);
        String className = EMPTY_VALUE;

        for (int i = 0; i < queryArr.length - 1; i++) {
            if (isQueryContainKeyWords(queryArr[i])) {
                className = PATH_TO_CLASS + firstUpperCase(queryArr[i + 1]);
            }
        }
        return Class.forName(className);
    }

    private boolean isQueryContainKeyWords(String query) {
        return isContainsString(query, "FROM") || isContainsString(query, "INTO") || isContainsString(query, "TABLE");
    }

    private String getPhraseDependingLetterCase(String queryString, String phrase) {
        if (queryString.contains(phrase.toUpperCase())) {
            return phrase.toUpperCase();
        } else {
            return phrase.toLowerCase();
        }
    }

    private String getSearchValues(String queryString) {
        return StringUtils.substringAfter(queryString, getPhraseDependingLetterCase(queryString, "WHERE") + REGEX_SPACE)
                .replace(";", EMPTY_VALUE)
                .replace(" = ", ": ")
                .replace(REGEX_SPACE + getPhraseDependingLetterCase(queryString, "AND") + REGEX_SPACE, REGEX_COMMA_AND_SPACE)
                .replace("'", "\"");
    }

    private List<Object> getObjectsByValues(String searchValues) {
        String[] searchValuesArr = Arrays.stream(searchValues.split(REGEX_COMMA_AND_SPACE))
                .map(s -> "\"" + StringUtils.substringBefore(s, ":") + "\":" +
                        StringUtils.substringAfter(s, ":")).toArray(String[]::new);

        return objects.stream()
                .filter(c -> isObjectContainsParams(c.toString(), searchValuesArr))
                .collect(Collectors.toList());
    }

    private List<Object> getObjectsWithParamsByValues(String searchParams, String searchValues) {
        List<Object> objectsWithParamsByValues = new ArrayList<>();

        for (Object obj : getObjectsByValues(searchValues)) {
            for (String s : obj.toString().split(",")) {
                for (String el : searchParams.split(REGEX_COMMA_AND_SPACE)) {
                    if (s.contains(el)) {
                        objectsWithParamsByValues.add(s);
                    }
                }
            }
        }
        return objectsWithParamsByValues;
    }

    private boolean isObjectContainsParams(String s1, String[] arr) {
        return Arrays.stream(arr).allMatch(s1::contains);
    }

    private Object getObjectFromString(String objectString) {
        JsonReader reader = new JsonReader(new StringReader(objectString));
        reader.setLenient(true);

        return new Gson().fromJson(reader, Object.class);
    }

    private List<String> getNewValuesListForUpdate(String queryString) {
        String[] newValuesArr = StringUtils.substringBetween(queryString,
                        getPhraseDependingLetterCase(queryString, "SET") + REGEX_SPACE,
                        REGEX_SPACE + getPhraseDependingLetterCase(queryString, "WHERE"))
                .replace(" = ", ": ")
                .replace("'", "\"")
                .split(REGEX_COMMA_AND_SPACE);

        return Stream.of(newValuesArr)
                .map(s -> "\"" + StringUtils.substringBefore(s, ":") + "\":" +
                        StringUtils.substringAfter(s, ":"))
                .collect(Collectors.toList());
    }

    private String getIdFromQuery(String queryString) {
        return StringUtils.substringAfter(queryString, "id = ").replaceAll(REGEX_NOT_NUMBERS, EMPTY_VALUE);
    }

    private String firstUpperCase(String word) {
        if (word == null || word.isEmpty()) return EMPTY_VALUE;
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }

    private boolean isContainsString(String sentence, String word) {
        return StringUtils.containsIgnoreCase(sentence, word);
    }
}



