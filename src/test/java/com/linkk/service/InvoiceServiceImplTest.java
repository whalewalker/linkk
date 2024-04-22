package com.linkk.service;

import com.linkk.data.model.Invoice;
import com.linkk.exception.ResourceNotFoundException;
import com.linkk.repo.InvoiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceImplTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    private Invoice invoice;

    @BeforeEach
    void setUp() {
        invoice = new Invoice();
        invoice.setInvoiceNumber("INV-0001");
        invoice.setAmount(100.0);
        invoice.setDueDate(LocalDateTime.now().plusDays(7));
    }

    @Test
    void testCreateInvoice() {
        when(invoiceRepository.save(invoice)).thenReturn(invoice);

        Invoice createdInvoice = invoiceService.createInvoice(invoice);

        assertNotNull(createdInvoice);
        assertEquals(invoice, createdInvoice);
        verify(invoiceRepository, times(1)).save(invoice);
    }

    @Test
    void testGetInvoiceById_ExistingInvoice() {
        Long invoiceId = 1L;
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));

        Invoice foundInvoice = invoiceService.getInvoiceById(invoiceId);

        assertNotNull(foundInvoice);
        assertEquals(invoice, foundInvoice);
        verify(invoiceRepository, times(1)).findById(invoiceId);
    }

    @Test
    void testGetInvoiceById_NonExistingInvoice() {
        Long invoiceId = 2L;
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> invoiceService.getInvoiceById(invoiceId));
        verify(invoiceRepository, times(1)).findById(invoiceId);
    }
}