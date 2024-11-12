package com.t1.testing.dto;

import com.t1.testing.entity.TaskStatus;
import jakarta.validation.constraints.Size;

public record TaskUpdateDto(

    @Size(min = 1, max = 255)
    String title,

    String description,

    TaskStatus status) {}
