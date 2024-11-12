package com.t1.testing.controller;

import com.t1.testing.dto.ApiError;
import com.t1.testing.exception.TaskNotFoundException;
import com.t1.testing.exception.TaskStatusException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalTime;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler({ConstraintViolationException.class,
        MethodArgumentNotValidException.class,
        IllegalStateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(Exception e) {
        return new ApiError(
                e.getMessage(),
                "Validation failed",
                HttpStatus.BAD_REQUEST,
                LocalTime.now().toString()
        );
    }

    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleTaskNotFoundException(Exception e) {
        return new ApiError(
                e.getMessage(),
                "Entity not found",
                HttpStatus.NOT_FOUND,
                LocalTime.now().toString()
        );
    }

    @ExceptionHandler(TaskStatusException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleTaskStatusException(Exception e) {
        return new ApiError(
                e.getMessage(),
                "Status conflict",
                HttpStatus.CONFLICT,
                LocalTime.now().toString()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleServerException(Exception e) {
        return new ApiError(
                e.getMessage(),
                "Server error",
                HttpStatus.INTERNAL_SERVER_ERROR,
                LocalTime.now().toString()
        );
    }
}
