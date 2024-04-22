package com.linkk.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Generic response data transfer object")
public class Response<T> {
    @Schema(description = "The response message")
    private String responseMessage;

    @Schema(description = "List of errors, if any")
    private List<Error> errors;

    @Schema(description = "List of model objects")
    private List<T> modelList;

    @Schema(description = "The total count of model objects")
    private long count;
}