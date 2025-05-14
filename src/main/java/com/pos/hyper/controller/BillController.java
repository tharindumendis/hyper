package com.pos.hyper.controller;

import com.pos.hyper.DTO.SaleInvoiceDto;
import com.pos.hyper.model.invoice.Invoice;
import com.pos.hyper.model.invoice.InvoiceMapper;
import com.pos.hyper.model.invoice_item.InvoiceItem;
import com.pos.hyper.model.invoice_item.InvoiceItemMapper;
import com.pos.hyper.model.product.ProductService;
import com.pos.hyper.repository.InvoiceItemRepository;
import com.pos.hyper.repository.InvoiceRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/bill")
public class BillController {
    private final ProductService productService;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final InvoiceMapper invoiceMapper;
    private final InvoiceItemMapper invoiceItemMapper;


    public BillController(ProductService productService, InvoiceRepository invoiceRepository, InvoiceItemRepository invoiceItemRepository, InvoiceMapper invoiceMapper, InvoiceItemMapper invoiceItemMapper) {
        this.productService = productService;
        this.invoiceRepository = invoiceRepository;
        this.invoiceItemRepository = invoiceItemRepository;
        this.invoiceMapper = invoiceMapper;
        this.invoiceItemMapper = invoiceItemMapper;
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Integer id, @RequestParam(required = true) String customer, @RequestParam(required = true) String invoice) {
        System.out.println("customer id = " + customer);
        System.out.println("invoice id = " + invoice);
        System.out.println("product id = " + id);
        Integer cId = Integer.parseInt(customer);
        Invoice theInvoice = invoiceRepository.findById(Integer.parseInt(invoice)).orElseThrow(()->new RuntimeException("Invoice not found"));
        if (!Objects.equals(theInvoice.getCustomer().getId(), cId)) {
            System.out.println("Invoice : " + theInvoice.getCustomer().getId() +  "invoice id = " + theInvoice.getId());
            throw new RuntimeException("Invoice not found with customer id " + cId);
        }
//        List<InvoiceItem> invoiceItems = invoiceItemRepository.findAllByInvoiceId(id);
//        List<Integer> productIds = invoiceItems.stream().map(InvoiceItem::getProduct).map(product -> product.getId()).toList();
//        if (!productIds.contains(id)) {
//            throw new RuntimeException("Product not found");
//        }
        return productService.getProductByIdForBill(id);
    }

    @GetMapping("/sale/{id}")
    public ResponseEntity<?> getSaleById(@PathVariable Integer id, @RequestParam(required = true) String customer) throws BadRequestException {
        Integer cId = Integer.parseInt(customer);
        if(cId == 1){
            throw new BadRequestException("unrecognized customer");
        }

        Invoice invoice = invoiceRepository.findById(id).orElseThrow(()->new BadRequestException("Invoice not found"));
        if (!Objects.equals(invoice.getCustomer().getId(), cId)) {
            throw new BadRequestException("Invoice not found");
        }
        List<InvoiceItem> invoiceItems = invoiceItemRepository.findAllByInvoiceId(id);
        return ResponseEntity.ok(new SaleInvoiceDto(invoiceMapper.toInvoiceDto(invoice), invoiceItemMapper.toDtoList(invoiceItems)));
    }







}
