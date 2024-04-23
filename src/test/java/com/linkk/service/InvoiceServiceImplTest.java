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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceImplTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    private Invoice invoice;

    @BeforeEach
    void setUp() {
        invoice = new Invoice();
        invoice.setId(1L);
        invoice.setAmount(100.0);
    }

    @Test
    void testCreateInvoice() {
        when(invoiceRepository.save(invoice)).thenReturn(invoice);
        Invoice createdInvoice = invoiceService.createInvoice(invoice);
        assertEquals(invoice, createdInvoice);
    }

    @Test
    void testGetInvoiceById_ExistingInvoice() {
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
        Invoice foundInvoice = invoiceService.getInvoiceById(1L);
        assertEquals(invoice, foundInvoice);
    }

    @Test
    void testGetInvoiceById_NonExistingInvoice() {
        when(invoiceRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> invoiceService.getInvoiceById(2L));
    }
}