package com.axonactive.agileskills.base.exception;

import lombok.Getter;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;

@Getter
@ApplicationException
public class ResourceNotFoundException extends Exception {

    private final transient ResponseBody responseBody;

    public ResourceNotFoundException(String keyMessage, String message) {
        this.responseBody = new ResponseBody(Response.Status.NOT_FOUND, keyMessage, message);
    }
}
