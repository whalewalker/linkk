package com.linkk.service.contract;

import com.linkk.data.dto.InvoiceDTO;
import com.linkk.data.model.Invoice;

public interface InvoiceService {
    Invoice createInvoice(InvoiceDTO invoiceDTO);
    Invoice getInvoiceById(Long id);
}