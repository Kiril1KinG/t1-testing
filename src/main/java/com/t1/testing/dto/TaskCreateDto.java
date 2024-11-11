package com.t1.testing.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public record TaskCreateDto(

    @NotEmpty
    @Max(255)
    String title,

    @NotEmpty
    String description) {}
