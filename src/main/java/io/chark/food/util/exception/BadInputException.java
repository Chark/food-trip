package io.chark.food.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadInputException extends RuntimeException {

    public BadInputException(String message, Object... args) {
        super(String.format(message, args));
    }
}