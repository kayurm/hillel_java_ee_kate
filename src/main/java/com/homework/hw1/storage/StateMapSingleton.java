package com.homework.hw1.storage;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class StateMapSingleton {

    private static StateMapSingleton single_instance = null;
    private static final Logger logger = LogManager.getLogger(StateMapSingleton.class);
    private static Map<Integer, String> txtMap;
    private static Integer keyId = 1;

    public static StateMapSingleton getInstance() {
        if (single_instance == null) {
            single_instance = new StateMapSingleton();
            txtMap = new HashMap<>();
        }
        return single_instance;
    }

    public Integer addValue(String value) {
        txtMap.put(keyId, value);
        Integer addedKey = keyId;
        keyId += 1;
        logger.info("added to Map: " + value);
        return addedKey;
    }

    public void modifyValue(Integer key, String body) {
        logger.info("modifying value by key: " + key);
        txtMap.replace(key, body);
    }

    public void deleteValue(Integer key) {
        logger.info("deleting value by key: " + key);
        txtMap.remove(key);
    }

    public String getAllMapValuesAsString() {
        return txtMap.keySet().stream()
                .map(key -> key + "=" + txtMap.get(key))
                .collect(Collectors.joining(", \n", "{", "}"));
    }

    public String getMapValue(Integer key) {
        if (!doesKeyExist(key)) return "Such key doesn't exist";
        return key + "=" + txtMap.get(key);
    }

    public Integer getMapKey(String body) {
        if (doesValueExist(body)) {
            for (Integer key : txtMap.keySet()) {
                if (txtMap.get(key).equals(body)) {
                    return key;
                }
            }
        }
        return null;
    }

    public boolean doesKeyExist(Integer key) {
        return txtMap.containsKey(key);
    }

    public boolean doesValueExist(String value) {
        return txtMap.containsValue(value);
    }
}
