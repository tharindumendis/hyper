package com.pos.hyper.model.invoice;

import com.pos.hyper.DTO.InvoiceDto;
import com.pos.hyper.model.PaymentMethod;
import com.pos.hyper.model.customer.Customer;
import org.springframework.stereotype.Service;

@Service
public class InvoiceMapper {

    private final Customer customer = new Customer();

    public InvoiceDto toInvoiceDto(Invoice invoice) {
        return new InvoiceDto(
                invoice.getId(),
                invoice.getCustomer().getId(),
                invoice.getTotal(),
                getPaymentMethod(invoice),
                invoice.getCreatedAt(),
                invoice.getUpdatedAt()

        );
    }
    public Invoice toInvoice(InvoiceDto invoiceDto) {
        customer.setId(invoiceDto.customerId());

        Invoice invoice = new Invoice();
        invoice.setId(invoiceDto.id());
        invoice.setCustomer(customer);
        invoice.setTotal(invoiceDto.total());
        invoice.setPaymentMethod(getPaymentMethod(invoiceDto));
        return invoice;
    }
    public Invoice toInvoice(InvoiceDto invoiceDto, Invoice invoice) {
        customer.setId(invoiceDto.customerId());
        invoice.setCustomer(customer);
        invoice.setTotal(invoiceDto.total());
        invoice.setPaymentMethod(getPaymentMethod(invoiceDto));
        return invoice;
    }
    public Invoice toInvoice(InvoiceDto invoiceDto, Invoice invoice, Customer customer) {
        invoice.setCustomer(customer);
        invoice.setTotal(invoiceDto.total());
        invoice.setPaymentMethod(getPaymentMethod(invoiceDto));
        return invoice;
    }

    private String getPaymentMethod(Invoice invoice) {
        try{
            return invoice.getPaymentMethod().toString();
        }catch(Exception e){
            return "CASH";
        }
    }
    private PaymentMethod getPaymentMethod(String paymentMethod) {
        try{
            return PaymentMethod.valueOf(paymentMethod);
        }catch(Exception e){
            return PaymentMethod.valueOf("CASH");
        }
    }
    private PaymentMethod getPaymentMethod(InvoiceDto invoiceDto) {
        try{
            return PaymentMethod.valueOf(invoiceDto.paymentMethod());
        }catch(Exception e){
            return PaymentMethod.valueOf("CASH");
        }
    }

}
