package com.pds.web.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
    private ResponseHandler(){
        //
    }

    public static <T> ResponseEntity<T> generateResponse(String message, HttpStatus status, T data) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("status", status.value());
        map.put("data", data);

        return new ResponseEntity<>((T) map,status);
    }
    public static <T> ResponseEntity<T> generateResponse(String message ,int code , HttpStatus status, T data, String uri) {
        Map<String, Object> map = new HashMap<>();
        map.put("path",uri);
        map.put("message", message);
        map.put("code", code);
        map.put("data", data);
        return new ResponseEntity<>((T) map,status);
    }
}