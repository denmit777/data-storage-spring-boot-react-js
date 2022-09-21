package com.training.data_storage.controller;

import com.training.data_storage.service.DataStorageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping
@AllArgsConstructor
public class CityController {

    private final DataStorageService dataStorageService;

    @PostMapping
    public ResponseEntity<?> createTable(@RequestBody String queryString)
            throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Object newTable = dataStorageService.getObjectFromQueryString(queryString);

        return ResponseEntity.status(CREATED).body(newTable);
    }

    @PostMapping("/cityInfo")
    public ResponseEntity<?> save(@RequestBody String queryString)
            throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException {
        Object savedObject = dataStorageService.getObjectFromQueryString(queryString);

        return ResponseEntity.status(CREATED)
                .body(savedObject);
    }

    @GetMapping("/cityInfo")
    public ResponseEntity<?> getById(@RequestParam(value = "queryString", defaultValue = "") String queryString)
            throws ClassNotFoundException, InvocationTargetException, InstantiationException, NoSuchMethodException, IllegalAccessException {
        Object currentObject = dataStorageService.getObjectFromQueryString(queryString);

        return ResponseEntity.ok(currentObject);
    }

    @PutMapping("/cityInfo")
    public ResponseEntity<?> update(@RequestBody String queryString)
            throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Object updatedObject = dataStorageService.getObjectFromQueryString(queryString);

        return ResponseEntity.ok(updatedObject);
    }

    @GetMapping("/cities")
    public ResponseEntity<?> getAll(@RequestParam(value = "queryString", defaultValue = "") String queryString)
            throws ClassNotFoundException, InvocationTargetException, InstantiationException, NoSuchMethodException, IllegalAccessException {

        List<Object> objects = dataStorageService.getObjectsFromQueryString(queryString);

        return ResponseEntity.ok(objects);
    }

    @DeleteMapping("/cities")
    public ResponseEntity<?> delete(@RequestParam(value = "queryString", defaultValue = "") String queryString)
            throws ClassNotFoundException, InvocationTargetException, InstantiationException, NoSuchMethodException, IllegalAccessException {
        List<Object> objects = dataStorageService.getObjectsFromQueryString(queryString);

        return ResponseEntity.ok(objects);
    }
}