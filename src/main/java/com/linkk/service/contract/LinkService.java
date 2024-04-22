package com.linkk.service.contract;

import com.linkk.data.model.Invoice;
import com.linkk.data.model.Link;

public interface LinkService {
    Link generateViewInvoiceLink(Long invoiceId);
    Link generatePayInvoiceLink(Long invoiceId);
    Invoice getInvoiceByToken(String token);
}