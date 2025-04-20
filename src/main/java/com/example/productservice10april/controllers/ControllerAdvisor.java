package com.example.productservice10april.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


//can be used to handles multiple exceptions in a single class
//ControllerAdvice is used to handle exceptions globally in the application
//various different types of exceptions can be handled in a single class using the ControllerAdvice in the application
//Also another way around is to create a response entity to get the response from the server
//these can be done on the controller level using the response entity and the controller advice


@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleException(Exception ex){
        // handle the exception
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleException2(Exception ex2){
        return new ResponseEntity<>(ex2.getMessage(), HttpStatus.CONFLICT);
    }




}
