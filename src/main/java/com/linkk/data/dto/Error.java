package com.linkk.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Represents an error object")
public class Error {

    @Schema(description = "The error code")
    private String code;

    @Schema(description = "The error message")
    private String message;
}