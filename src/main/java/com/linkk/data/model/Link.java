package com.linkk.data.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "links")
@Data
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    @Schema(description = "The type of the link (VIEW_INVOICE or PAY_INVOICE)")
    private LinkType type;

    @Column(nullable = false)
    @Schema(description = "The expiration date of the link")
    private LocalDateTime expirationDate;

    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;
}