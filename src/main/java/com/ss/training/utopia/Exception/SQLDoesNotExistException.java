package com.ss.training.utopia.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class SQLDoesNotExistException extends IllegalStateException {
    private final String id;

    public SQLDoesNotExistException(String type, String id) {
        super(type + " with the id '" + id + "' does not exist.");
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
