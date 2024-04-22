package com.linkk.controller;

import com.linkk.data.dto.Response;
import com.linkk.data.model.Link;
import com.linkk.service.contract.LinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Link Controller", description = "Endpoints for managing links")
@RestController
@RequestMapping("/api/v1/link")
@RequiredArgsConstructor
public class LinkController {
    private final LinkService linkService;

    @Operation(summary = "Generate a view invoice link", description = "Generates a unique link for viewing an invoice")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "View invoice link generated successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Link.class ))}),
            @ApiResponse(responseCode = "404", description = "Invoice not found", content = @Content)
    })
    @PostMapping("/invoices/{invoiceId}/view")
    public ResponseEntity<Response<Link>> generateViewInvoiceLink(@PathVariable Long invoiceId) {
        Response<Link> link = linkService.generateViewInvoiceLink(invoiceId);
        return ResponseEntity.ok(link);
    }

    @Operation(summary = "Generate a pay invoice link", description = "Generates a unique link for paying an invoice")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pay invoice link generated successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Link.class))}),
            @ApiResponse(responseCode = "404", description = "Invoice not found", content = @Content)
    })
    @PostMapping("/invoices/{invoiceId}/pay")
    public ResponseEntity<Response<Link>> generatePayInvoiceLink(@PathVariable Long invoiceId) {
        Response<Link> link = linkService.generatePayInvoiceLink(invoiceId);
        return ResponseEntity.ok(link);
    }
}