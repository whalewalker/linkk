package com.linkk.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Schema(description = "Represents an invoice DTO")
@Data
public class Invoice {
    @Schema(description = "The invoice number", example = "INV-0001")
    private String invoiceNumber;

    @Schema(description = "The invoice amount", example = "100.00")
    private Double amount;

    @Schema(description = "The due date of the invoice", example = "2023-05-01")
    private LocalDate dueDate;
}