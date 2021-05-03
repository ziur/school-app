package com.ziur.school.api.rest;

import lombok.Getter;

import java.util.Date;

public class ErrorResponse {
    @Getter
    private final int code;
    @Getter
    private String message;
    @Getter
    private String details;
    @Getter
    private final Date timestamp;

    public ErrorResponse(int code) {
        this.timestamp = new Date();
        this.code = code;
    }

    public ErrorResponse message(String message) {
        this.message = message;
        return this;
    }

    public ErrorResponse details(String details) {
        this.details = details;
        return this;
    }
}
