package com.ss.training.utopia.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class SQLAlreadyExistsException extends IllegalStateException {
    private final String id;

    public SQLAlreadyExistsException(String type, String id) {
        super(type + " with the id '" + id + "' already exists.");
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
