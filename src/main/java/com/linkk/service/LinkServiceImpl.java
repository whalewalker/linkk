package com.linkk.service;

import com.linkk.data.model.Invoice;
import com.linkk.data.model.Link;
import com.linkk.data.model.LinkType;
import com.linkk.exception.ResourceNotFoundException;
import com.linkk.repo.InvoiceRepository;
import com.linkk.repo.LinkRepository;
import com.linkk.service.contract.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {
    private final InvoiceRepository invoiceRepository;
    private final LinkRepository linkRepository;

    @Override
    public Link generateViewInvoiceLink(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        String token = generateUniqueToken();
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(7); // Link expires in 7 days

        Link link = new Link();
        link.setToken(token);
        link.setType(LinkType.VIEW_INVOICE);
        link.setExpirationDate(expirationDate);
        link.setInvoice(invoice);

        return linkRepository.save(link);
    }

    @Override
    public Link generatePayInvoiceLink(Long invoiceId) {
        return null;
    }

    @Override
    public Invoice getInvoiceByToken(String token) {
        Link link = linkRepository.findByToken(token);
        if (link == null || link.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new ResourceNotFoundException("Invalid or expired link");
        }
        return link.getInvoice();
    }

    private String generateUniqueToken() {
        return UUID.randomUUID().toString();
    }
}