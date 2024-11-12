package com.t1.testing.dto;

import com.t1.testing.entity.TaskStatus;

public record TaskDto (

    Long id,

    String title,

    String description,

    TaskStatus status) {}
