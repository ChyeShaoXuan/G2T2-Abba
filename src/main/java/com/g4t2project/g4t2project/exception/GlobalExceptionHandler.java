package com.g4t2project.g4t2project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    // @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    // @ResponseBody
    // public String handleInvalidInput(Exception e) {
    //     return e.getMessage();
    // }

    //Handle NoAvailableWorkerException
    // @ExceptionHandler(NoAvailableWorkerException.class)
    // @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // or another appropriate status
    // @ResponseBody
    // public String handleNoAvailableWorker(NoAvailableWorkerException e) {
    //     return e.getMessage(); // Return the message from the exception
    // }

    @ExceptionHandler(NoAvailableWorkerException.class)
    public ResponseEntity<String> handleNoAvailableWorkerException(NoAvailableWorkerException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(ManualBookingRequiredException.class)
    public ResponseEntity<String> handleManualBookingRequiredException(ManualBookingRequiredException ex) {
        // This will return a 400 Bad Request with a custom message
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        // Directly pass the exception's message to the frontend
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        // Handle any other unexpected exceptions
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());
    }
}
