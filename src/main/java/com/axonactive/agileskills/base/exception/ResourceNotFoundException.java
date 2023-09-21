package com.axonactive.agileskills.base.exception;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.Getter;

@Getter
@ApplicationException
public class ResourceNotFoundException extends Exception {

    private final transient ResponseBody responseBody;

    public ResourceNotFoundException(String keyMessage, String message) {
        this.responseBody = new ResponseBody(Response.Status.NOT_FOUND, keyMessage, message);
    }
}
