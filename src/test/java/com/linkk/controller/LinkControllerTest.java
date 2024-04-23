package com.linkk.controller;

import com.linkk.data.dto.Response;
import com.linkk.data.model.Link;
import com.linkk.service.contract.LinkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LinkController.class)
 class LinkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LinkService linkService;

    @Test
     void testGenerateViewInvoiceLinkSuccess() throws Exception {
        Long invoiceId = 1L;
        Link link = new Link();
        List<Link> linkList = List.of(link);
        Response<Link> response = new Response<>("View invoice link generated successfully", null, linkList, linkList.size());

        when(linkService.generateViewInvoiceLink(invoiceId)).thenReturn(response);

        mockMvc.perform(post("/links/invoices/{invoiceId}/view", invoiceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseMessage").value("View invoice link generated successfully"))
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.modelList[0]").exists())
                .andExpect(jsonPath("$.count").value(1));
    }

    @Test
     void testGenerateViewInvoiceLinkInvoiceNotFound() throws Exception {
        Long invoiceId = 1L;
        Response<Link> response = new Response<>("Invoice not found", null, null, 0);

        when(linkService.generateViewInvoiceLink(invoiceId)).thenReturn(response);

        mockMvc.perform(post("/links/invoices/{invoiceId}/view", invoiceId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.responseMessage").value("Invoice not found"))
                .andExpect(jsonPath("$.errors[0].message").value("Invoice not found"))
                .andExpect(jsonPath("$.modelList").isEmpty())
                .andExpect(jsonPath("$.count").value(0));
    }

    @Test
     void testGeneratePayInvoiceLinkSuccess() throws Exception {
        Long invoiceId = 1L;
        Link link = new Link();
        List<Link> linkList = List.of(link);
        Response<Link> response = new Response<>("Pay invoice link generated successfully", null, linkList, linkList.size());

        when(linkService.generatePayInvoiceLink(invoiceId)).thenReturn(response);

        mockMvc.perform(post("/links/invoices/{invoiceId}/pay", invoiceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseMessage").value("Pay invoice link generated successfully"))
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.modelList[0]").exists())
                .andExpect(jsonPath("$.count").value(1));
    }

    @Test
     void testGeneratePayInvoiceLinkInvoiceNotFound() throws Exception {
        Long invoiceId = 1L;
        Response<Link> response = new Response<>("Invoice not found", null, null, 0);

        when(linkService.generatePayInvoiceLink(invoiceId)).thenReturn(response);

        mockMvc.perform(post("/links/invoices/{invoiceId}/pay", invoiceId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.responseMessage").value("Invoice not found"))
                .andExpect(jsonPath("$.errors[0].message").value("Invoice not found"))
                .andExpect(jsonPath("$.modelList").isEmpty())
                .andExpect(jsonPath("$.count").value(0));
    }
}