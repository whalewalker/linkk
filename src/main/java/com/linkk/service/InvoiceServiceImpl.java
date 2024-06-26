package com.linkk.service;

import com.linkk.data.dto.InvoiceDTO;
import com.linkk.data.model.Invoice;
import com.linkk.exception.ResourceNotFoundException;
import com.linkk.repo.InvoiceRepository;
import com.linkk.service.contract.InvoiceService;
import org.springframework.stereotype.Service;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public Invoice createInvoice(InvoiceDTO invoiceDTO) {
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(invoiceDTO.getInvoiceNumber());
        invoice.setAmount(invoiceDTO.getAmount());
        invoice.setDueDate(invoiceDTO.getDueDate());
        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice getInvoiceById(Long id) {
        return invoiceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
    }

}