package com.pos.hyper.controller;


import com.pos.hyper.model.inOrder.InOrder;
import com.pos.hyper.model.inOrder.InOrderDto;
import com.pos.hyper.model.invoice.Invoice;
import com.pos.hyper.model.product.Product;
import com.pos.hyper.repository.InOrderRepository;
import com.pos.hyper.repository.InvoiceRepository;
import com.pos.hyper.validation.InOrderValidation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class InOrderController {

    private final InOrderRepository inOrderRepository;
    private final InOrderValidation inOrderValidation;
    private final InvoiceRepository invoiceRepository;

    public InOrderController(InOrderRepository inOrderRepository, InOrderValidation inOrderValidation, InvoiceRepository invoiceRepository) {
        this.inOrderRepository = inOrderRepository;
        this.inOrderValidation = inOrderValidation;
        this.invoiceRepository = invoiceRepository;
    }


    @GetMapping("")
    List<InOrder> getInOrders() {
        return inOrderRepository.findAll();
    }

    @GetMapping("/{id}")
    InOrder getInOrder(@PathVariable Integer id) {
        return inOrderRepository.findById(id).get();
    }
    @GetMapping("/invoice/{id}")
    List<InOrder> getInOrdersByInvoiceId(@PathVariable Integer id) {
        return inOrderRepository.findAllByInvoiceId(id);
    }
    @GetMapping("/product/{id}")
    List<InOrder> getInOrdersByProductId(@PathVariable Integer id) {

        return inOrderRepository.findAllByProductId(id);
    }

    @PostMapping("")
    InOrder save(@Valid @RequestBody InOrderDto inOrderDto) {
        System.out.println(inOrderDto);
        InOrder inOrder = new InOrder();
        Invoice invoice =  invoiceRepository.findById(inOrderDto.invoiceId()).get();
        Product product = new Product();

        inOrder.setId(inOrderDto.id());
        product.setId(inOrderDto.productId());

        inOrder.setProduct(product);
        inOrder.setInvoice(invoice);

        inOrder.setQuantity(inOrderDto.quantity());
        inOrder.setUnitPrice(inOrderDto.unitPrice());

//        inOrder = inOrderValidation.inOrderValidate(inOrder);

        return inOrderRepository.save(inOrder);
    }
    @PutMapping("/{id}")
    InOrder update(@Valid @RequestBody InOrder order, @PathVariable Integer id) {
        if (inOrderRepository.existsById(id)) {
            order.setId(id);
            order = inOrderValidation.inOrderValidate(order);
            return inOrderRepository.save(order);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "P.Id : "+id+" Not Found");
        }
    }
    @PutMapping("")
    ResponseEntity<?> update(@RequestBody InOrder order) {
        order = inOrderValidation.inOrderValidate(order);
        inOrderRepository.save(order);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Integer id) {
        inOrderRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
