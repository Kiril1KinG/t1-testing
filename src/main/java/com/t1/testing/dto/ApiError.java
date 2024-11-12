package com.t1.testing.dto;

import org.springframework.http.HttpStatus;

public record ApiError(

    String message,

    String reason,

    HttpStatus status,

    String timestamp) {}
