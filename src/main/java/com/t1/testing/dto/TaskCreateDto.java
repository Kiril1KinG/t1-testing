package com.t1.testing.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record TaskCreateDto(

    @NotEmpty
    @Size(min = 1, max = 255)
    String title,

    @NotEmpty
    String description) {}
