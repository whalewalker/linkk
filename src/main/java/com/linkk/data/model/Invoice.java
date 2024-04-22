package com.linkk.data.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@Schema(description = "Represents an invoice")
@Data
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Schema(description = "The invoice number", example = "INV-0001")
    private String invoiceNumber;

    @Column(nullable = false)
    @Schema(description = "The invoice amount", example = "100.00")
    private Double amount;

    @Column(nullable = false)
    @Schema(description = "The due date of the invoice", example = "2023-05-01T12:00:00")
    private LocalDateTime dueDate;
}