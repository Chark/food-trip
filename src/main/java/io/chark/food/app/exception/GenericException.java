package io.chark.food.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class GenericException extends RuntimeException {

    public GenericException(String message, Object... args) {
        super(String.format(message, args));
    }
}