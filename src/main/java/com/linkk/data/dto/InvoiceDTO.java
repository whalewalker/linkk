package com.linkk.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Schema(description = "Represents an invoice DTO")
@Data
public class InvoiceDTO {
    @Schema(description = "The invoice number", example = "INV-0001")
    @NotBlank(message = "invoice number cannot be blank")
    private String invoiceNumber;

    @Schema(description = "The invoice amount", example = "100.00")
    @Positive(message = "amount must be valid")
    private Double amount;

    @Schema(description = "The due date of the invoice", example = "2023-05-01")
    @PastOrPresent(message = "dueDate must be valid")
    private LocalDate dueDate;
}