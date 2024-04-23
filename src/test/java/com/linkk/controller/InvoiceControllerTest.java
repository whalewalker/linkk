package com.linkk.controller;


import com.linkk.data.dto.Response;
import com.linkk.data.model.Invoice;
import com.linkk.service.contract.InvoiceService;
import com.linkk.service.contract.LinkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InvoiceController.class)
 class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvoiceService invoiceService;

    @MockBean
    private LinkService linkService;

    @Test
     void testGetInvoiceByTokenSuccess() throws Exception {
        String token = "valid-token";
        Invoice invoice = new Invoice();
        List<Invoice> invoiceList = List.of(invoice);
        Response<Invoice> response = new Response<>("Success", null, invoiceList, invoiceList.size());

        when(linkService.getInvoiceByToken(token)).thenReturn(response);

        mockMvc.perform(get("/invoices/{token}", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modelList[0]").exists())
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.responseMessage").value("Success"))
                .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
     void testGetInvoiceByTokenInvalidToken() throws Exception {
        String token = "invalid-token";
        Response<Invoice> response = new Response<>("Link token is invalid or expired", null, null, 0);

        when(linkService.getInvoiceByToken(token)).thenReturn(response);

        mockMvc.perform(get("/invoices/{token}", token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.responseMessage").value("Link token is invalid or expired"))
                .andExpect(jsonPath("$.errors[0].message").value("Invalid token"))
                .andExpect(jsonPath("$.modelList").isEmpty())
                .andExpect(jsonPath("$.count").value(0));
    }

    @Test
     void testCreateInvoiceSuccess() throws Exception {
        Invoice invoice = new Invoice();
        Invoice createdInvoice = new Invoice();
        List<Invoice> invoiceList = List.of(createdInvoice);
        Response<Invoice> response = new Response<>("Invoice created successfully", null, invoiceList, invoiceList.size());

        when(invoiceService.createInvoice(any(Invoice.class))).thenReturn(createdInvoice);

        mockMvc.perform(post("/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"amount\":100.0}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.responseMessage").value("Invoice created successfully"))
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.modelList[0].id").value(createdInvoice.getId()))
                .andExpect(jsonPath("$.count").value(1));
    }

    @Test
     void testCreateInvoiceInvalidInput() throws Exception {
        Response<Invoice> response = new Response<>("Invalid input data", null, null, 0);

        mockMvc.perform(post("/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":100.0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.responseMessage").value("Invalid input data"))
                .andExpect(jsonPath("$.errors[0].message").value("Invalid input data"))
                .andExpect(jsonPath("$.modelList").isEmpty())
                .andExpect(jsonPath("$.count").value(0));
    }
}