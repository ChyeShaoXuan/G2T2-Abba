package com.g4t2project.g4t2project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleInvalidInput(Exception e) {
        return e.getMessage();
    }

    //Handle NoAvailableWorkerException
    @ExceptionHandler(NoAvailableWorkerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // or another appropriate status
    @ResponseBody
    public String handleNoAvailableWorker(NoAvailableWorkerException e) {
        return e.getMessage(); // Return the message from the exception
    }

    // Handle other exceptions as needed
}
