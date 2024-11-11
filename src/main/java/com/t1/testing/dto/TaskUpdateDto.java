package com.t1.testing.dto;

import com.t1.testing.entity.TaskStatus;
import jakarta.validation.constraints.Max;

public record TaskUpdateDto(

    @Max(255)
    String title,

    String description,

    TaskStatus status) {}
