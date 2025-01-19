/*
 * © 2024 bykowski. All rights reserved.
 *
 * This file is part of the Academa project.
 * You may not use this file except in compliance with the project license.
 *
 * Created on: 2024-11-09
 * File: ErrorDetails.java
 *
 * Last modified: 2024-11-09 15:28:38
 */

package dev.bykowski.academa.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDetails {
    private Date timestamp;

    private String message;

    private String details;

    private Map<String, String> fields;

    public ErrorDetails(Date timestamp, String message, String details, Map<String, String> fields) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
        this.fields = fields;
    }
}