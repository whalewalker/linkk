package com.linkk.service.contract;

import com.linkk.data.dto.Response;
import com.linkk.data.model.Invoice;
import com.linkk.data.model.Link;

public interface LinkService {
    Response<Link> generateViewInvoiceLink(Long invoiceId);
    Response<Link> generatePayInvoiceLink(Long invoiceId);
    Response<Invoice> getInvoiceByToken(String token);
}