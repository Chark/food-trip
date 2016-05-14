package io.chark.food.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    /**
     * Create not found exception for provided entity.
     *
     * @param clazz class to construct the exception for.
     * @param id    what id caused the exception.
     */
    public NotFoundException(Class clazz, long id) {
        this("%s{id=%s} is not found", clazz.getSimpleName(), id);
    }

    public NotFoundException(String message, Object... args) {
        super(String.format(message, args));
    }
}