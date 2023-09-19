package com.axonactive.agileskills.base.exception;

import lombok.Getter;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;

@Getter
@ApplicationException
public class InputValidationException extends Exception {

    private final transient ResponseBody responseBody;

    public InputValidationException(String keyMessage, String message) {
        this.responseBody = new ResponseBody(Response.Status.BAD_REQUEST, keyMessage, message);
    }
}
