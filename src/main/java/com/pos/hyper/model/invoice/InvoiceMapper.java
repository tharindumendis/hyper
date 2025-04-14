package com.pos.hyper.model.invoice;

import com.pos.hyper.model.customer.Customer;
import org.springframework.stereotype.Service;

@Service
public class InvoiceMapper {

    private final Customer customer = new Customer();

    public InvoiceDto toInvoiceDto(Invoice invoice) {
        return new InvoiceDto(
                invoice.getId(),
                invoice.getCustomer().getId(),
                invoice.getTotal()
        );
    }
    public Invoice toInvoice(InvoiceDto invoiceDto) {
        customer.setId(invoiceDto.customerId());

        Invoice invoice = new Invoice();
        invoice.setId(invoiceDto.id());
        invoice.setCustomer(customer);
        invoice.setTotal(invoiceDto.total());
        return invoice;
    }
    public Invoice toInvoice(InvoiceDto invoiceDto, Invoice invoice) {
        customer.setId(invoiceDto.customerId());
        invoice.setCustomer(customer);
        invoice.setTotal(invoiceDto.total());
        return invoice;
    }
    public Invoice toInvoice(InvoiceDto invoiceDto, Invoice invoice, Customer customer) {
        invoice.setCustomer(customer);
        invoice.setTotal(invoiceDto.total());
        return invoice;
    }



}
