package com.linkk.controller;

import com.linkk.data.model.Invoice;
import com.linkk.data.model.Link;
import com.linkk.exception.ResourceNotFoundException;
import com.linkk.service.contract.InvoiceService;
import com.linkk.service.contract.LinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Invoice Controller", description = "Endpoints for managing invoices")
@RestController
@RequestMapping("/api/links")
public class LinkController {
    private final LinkService linkService;
    private final InvoiceService invoiceService;

    public LinkController(LinkService linkService, InvoiceService invoiceService) {
        this.linkService = linkService;
        this.invoiceService = invoiceService;
    }

    @Operation(summary = "Generate a view invoice link", description = "Generates a unique link for viewing an invoice")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "View invoice link generated successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Link.class ))}),
            @ApiResponse(responseCode = "404", description = "Invoice not found", content = @Content)
    })
    @PostMapping("/invoices/{invoiceId}/view")
    public ResponseEntity<Link> generateViewInvoiceLink(@PathVariable Long invoiceId) {
        Link link = linkService.generateViewInvoiceLink(invoiceId);
        return ResponseEntity.ok(link);
    }

    @Operation(summary = "Generate a pay invoice link", description = "Generates a unique link for paying an invoice")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pay invoice link generated successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Link.class))}),
            @ApiResponse(responseCode = "404", description = "Invoice not found", content = @Content)
    })
    @PostMapping("/invoices/{invoiceId}/pay")
    public ResponseEntity<Link> generatePayInvoiceLink(@PathVariable Long invoiceId) {
        Link link = linkService.generatePayInvoiceLink(invoiceId);
        return ResponseEntity.ok(link);
    }

    @Operation(summary = "Get invoice by token", description = "Retrieves an invoice by the provided link token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invoice retrieved successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Invoice.class))}),
            @ApiResponse(responseCode = "404", description = "Link token is invalid or expired", content = @Content)
    })
    @GetMapping("/{token}")
    public ResponseEntity<?> getInvoiceByToken(@PathVariable String token) {
        try {
            Invoice invoice = linkService.getInvoiceByToken(token);
            return ResponseEntity.ok(invoice);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @Operation(summary = "Create a new invoice", description = "Creates a new invoice")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Invoice created successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Invoice.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@RequestBody Invoice invoice) {
        Invoice createdInvoice = invoiceService.createInvoice(invoice);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInvoice);
    }
}