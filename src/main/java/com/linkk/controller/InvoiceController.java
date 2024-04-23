package com.linkk.controller;

import com.linkk.data.dto.Response;
import com.linkk.data.model.Invoice;
import com.linkk.data.dto.InvoiceDTO;
import com.linkk.service.contract.InvoiceService;
import com.linkk.service.contract.LinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.linkk.util.Utils.successfulResponse;

@Tag(name = "Invoice Controller", description = "Endpoints for managing invoices")
@RestController
@RequestMapping("/api/v1/invoice")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceService invoiceService;
    private final LinkService linkService;

    @Operation(summary = "Get invoice by token", description = "Retrieves an invoice by the provided link token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invoice retrieved successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Invoice.class))}),
            @ApiResponse(responseCode = "404", description = "Link token is invalid or expired", content = @Content)
    })
    @GetMapping("/{token}")
    public ResponseEntity<Response<Invoice>> getInvoiceByToken(@PathVariable String token) {
        Response<Invoice> invoice = linkService.getInvoiceByToken(token);
        return ResponseEntity.ok(invoice);
    }

    @Operation(summary = "Create a new invoice", description = "Creates a new invoice")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Invoice created successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Invoice.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Response<Invoice>> createInvoice(@Valid @RequestBody InvoiceDTO invoiceDTO) {
        Invoice createdInvoice = invoiceService.createInvoice(invoiceDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(successfulResponse(List.of(createdInvoice)));
    }
}
