package com.ota.notetaking.exceptions;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class NoteTakingServiceException extends ResponseStatusException {
    public NoteTakingServiceException(HttpStatusCode status, String reason) {
        super(status, reason);
    }
}
