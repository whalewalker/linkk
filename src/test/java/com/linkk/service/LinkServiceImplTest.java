package com.linkk.service;

import com.linkk.data.dto.Response;
import com.linkk.data.model.Invoice;
import com.linkk.data.model.Link;
import com.linkk.data.model.LinkType;
import com.linkk.exception.ResourceNotFoundException;
import com.linkk.repo.InvoiceRepository;
import com.linkk.repo.LinkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 public class LinkServiceImplTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private LinkRepository linkRepository;

    @InjectMocks
    private LinkServiceImpl linkService;

    private Invoice invoice;
    private final String successMessage = "Successful";

    @BeforeEach
     void setUp() {
        invoice = new Invoice();
        invoice.setId(1L);
        invoice.setAmount(100.0);
    }

    @Test
     void testGenerateViewInvoiceLink() {
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
        when(linkRepository.save(any(Link.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Response<Link> response = linkService.generateViewInvoiceLink(1L);
        assertEquals(successMessage, response.getResponseMessage());
        assertEquals(1, response.getModelList().size());

        Link link = response.getModelList().get(0);
        assertEquals(LinkType.VIEW_INVOICE, link.getType());
        assertEquals(invoice, link.getInvoice());
    }

    @Test
     void testGenerateViewInvoiceLinkInvalidInvoiceId() {
        when(invoiceRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> linkService.generateViewInvoiceLink(2L));
    }

    @Test
     void testGeneratePayInvoiceLink() {
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
        when(linkRepository.save(any(Link.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Response<Link> response = linkService.generatePayInvoiceLink(1L);
        assertEquals(successMessage, response.getResponseMessage());
        assertEquals(1, response.getModelList().size());

        Link link = response.getModelList().get(0);
        assertEquals(LinkType.PAY_INVOICE, link.getType());
        assertEquals(invoice, link.getInvoice());
    }

    @Test
     void testGeneratePayInvoiceLinkInvalidInvoiceId() {
        when(invoiceRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> linkService.generatePayInvoiceLink(2L));
    }

    @Test
     void testGetInvoiceByToken() {
        String token = UUID.randomUUID().toString();
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(1);
        Link link = new Link();
        link.setToken(token);
        link.setExpirationDate(expirationDate);
        link.setInvoice(invoice);

        when(linkRepository.findByToken(token)).thenReturn(link);

        Response<Invoice> response = linkService.getInvoiceByToken(token);
        assertEquals(successMessage, response.getResponseMessage());
        assertEquals(1, response.getModelList().size());
        assertEquals(invoice, response.getModelList().get(0));
    }

    @Test
     void testGetInvoiceByTokenInvalidToken() {
        when(linkRepository.findByToken("invalid")).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> linkService.getInvoiceByToken("invalid"));
    }

    @Test
     void testGetInvoiceByTokenExpiredLink() {
        String token = UUID.randomUUID().toString();
        LocalDateTime expirationDate = LocalDateTime.now().minusDays(1);
        Link link = new Link();
        link.setToken(token);
        link.setExpirationDate(expirationDate);
        link.setInvoice(invoice);

        when(linkRepository.findByToken(token)).thenReturn(link);
        assertThrows(ResourceNotFoundException.class, () -> linkService.getInvoiceByToken(token));
    }
}