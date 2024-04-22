package com.linkk.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkk.data.model.Invoice;
import com.linkk.data.model.Link;
import com.linkk.exception.ResourceNotFoundException;
import com.linkk.service.contract.InvoiceService;
import com.linkk.service.contract.LinkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LinkController.class)
class LinkControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LinkService linkService;

    @MockBean
    private InvoiceService invoiceService;

    @Test
    void testGenerateViewInvoiceLink() throws Exception {
        Long invoiceId = 1L;
        Link link = new Link();
        link.setId(invoiceId);
        link.setToken("view-token");
        when(linkService.generateViewInvoiceLink(invoiceId)).thenReturn(link);

        mockMvc.perform(post("/invoices/{invoiceId}/view", invoiceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("view-token"))
                .andExpect(jsonPath("$.invoiceId").value(invoiceId));
    }

    @Test
    void testGenerateViewInvoiceLinkInvoiceNotFound() throws Exception {
        Long invoiceId = 1L;
        when(linkService.generateViewInvoiceLink(invoiceId)).thenThrow(new ResourceNotFoundException("Invoice not found"));

        mockMvc.perform(post("/invoices/{invoiceId}/view", invoiceId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetInvoiceByToken() throws Exception {
        String token = "view-token";
        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setAmount(100.0);
        invoice.setInvoiceNumber("Test Invoice");

        when(linkService.getInvoiceByToken(token)).thenReturn(invoice);

        mockMvc.perform(get("/{token}", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(invoice.getId()))
                .andExpect(jsonPath("$.amount").value(invoice.getAmount()))
                .andExpect(jsonPath("$.dueDate").value(invoice.getDueDate().toString()));
    }

    @Test
    void testGetInvoiceByTokenInvalidToken() throws Exception {
        String token = "invalid-token";
        when(linkService.getInvoiceByToken(token)).thenThrow(new ResourceNotFoundException("Link token is invalid or expired"));

        mockMvc.perform(get("/{token}", token))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateInvoice() throws Exception {
        Invoice invoice = new Invoice();
        invoice.setAmount(100.0);

        Invoice createdInvoice = new Invoice();
        createdInvoice.setId(1L);
        createdInvoice.setAmount(100.0);

        when(invoiceService.createInvoice(any(Invoice.class))).thenReturn(createdInvoice);

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invoice)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(createdInvoice.getId()))
                .andExpect(jsonPath("$.amount").value(createdInvoice.getAmount()))
                .andExpect(jsonPath("$.dueDate").value(createdInvoice.getDueDate().toString()));
    }

    @Test
    void testGeneratePayInvoiceLinkInvalidInvoiceId() throws Exception {
        Long invoiceId = -1L; // Invalid invoice ID
        when(linkService.generatePayInvoiceLink(invoiceId)).thenThrow(new IllegalArgumentException("Invalid invoice ID"));

        mockMvc.perform(post("/invoices/{invoiceId}/pay", invoiceId))
                .andExpect(status().isBadRequest());
    }
}