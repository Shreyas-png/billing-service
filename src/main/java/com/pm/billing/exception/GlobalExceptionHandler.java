package com.pm.billing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceAlreadyExist.class)
    public ResponseEntity<ApiError> resourceAlreadyExistExceptionHandler(ResourceAlreadyExist ex){
        ApiError error = ApiError.builder()
                .error("BAD REQUEST")
                .message(ex.getMessage())
                .status(400)
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ApiError> resourceAlreadyExistExceptionHandler(ResourceNotFound ex){
        ApiError error = ApiError.builder()
                .error("BAD REQUEST")
                .message(ex.getMessage())
                .status(404)
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequest.class)
    public ResponseEntity<ApiError> badRequestExceptionHandler(BadRequest ex){
        ApiError error = ApiError.builder()
                .error("BAD REQUEST")
                .message(ex.getMessage())
                .status(404)
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
