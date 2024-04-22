package com.linkk.service.contract;

import com.linkk.data.model.Invoice;

public interface InvoiceService {
    Invoice createInvoice(Invoice invoice);
    Invoice getInvoiceById(Long id);
}