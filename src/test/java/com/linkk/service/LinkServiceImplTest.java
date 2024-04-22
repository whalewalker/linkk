package com.linkk.service;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LinkServiceImplTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private LinkRepository linkRepository;

    @InjectMocks
    private LinkServiceImpl linkService;

    private Invoice invoice;
    private Link link;

    @BeforeEach
    void setUp() {
        invoice = new Invoice();
        invoice.setId(1L);
        invoice.setInvoiceNumber("INV-0001");
        invoice.setAmount(100.0);
        invoice.setDueDate(LocalDateTime.now().plusDays(7));

        link = new Link();
        link.setId(1L);
        link.setToken(UUID.randomUUID().toString());
        link.setType(LinkType.VIEW_INVOICE);
        link.setExpirationDate(LocalDateTime.now().plusDays(7));
        link.setInvoice(invoice);
    }

    @Test
    void testGenerateViewInvoiceLink_ExistingInvoice() {
        Long invoiceId = 1L;
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
        when(linkRepository.save(any(Link.class))).thenReturn(link);

        Link generatedLink = linkService.generateViewInvoiceLink(invoiceId);

        assertNotNull(generatedLink);
        assertEquals(link.getToken(), generatedLink.getToken());
        assertEquals(LinkType.VIEW_INVOICE, generatedLink.getType());
        assertNotNull(generatedLink.getExpirationDate());
        assertEquals(invoice, generatedLink.getInvoice());
        verify(invoiceRepository, times(1)).findById(invoiceId);
        verify(linkRepository, times(1)).save(any(Link.class));
    }

    @Test
    void testGenerateViewInvoiceLink_NonExistingInvoice() {
        Long invoiceId = 2L;
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> linkService.generateViewInvoiceLink(invoiceId));
        verify(invoiceRepository, times(1)).findById(invoiceId);
        verify(linkRepository, never()).save(any(Link.class));
    }

    @Test
    void testGetInvoiceByToken_ValidToken() {
        String token = link.getToken();
        when(linkRepository.findByToken(token)).thenReturn(link);

        Invoice foundInvoice = linkService.getInvoiceByToken(token);

        assertNotNull(foundInvoice);
        assertEquals(invoice, foundInvoice);
        verify(linkRepository, times(1)).findByToken(token);
    }

    @Test
    void testGetInvoiceByToken_InvalidToken() {
        String token = "invalid-token";
        when(linkRepository.findByToken(token)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> linkService.getInvoiceByToken(token));
        verify(linkRepository, times(1)).findByToken(token);
    }

    @Test
    void testGetInvoiceByToken_ExpiredLink() {
        String token = link.getToken();
        link.setExpirationDate(LocalDateTime.now().minusDays(1)); // Set expiration date to yesterday
        when(linkRepository.findByToken(token)).thenReturn(link);

        assertThrows(ResourceNotFoundException.class, () -> linkService.getInvoiceByToken(token));
        verify(linkRepository, times(1)).findByToken(token);
    }
}