package com.pos.hyper.model.inOrder;

import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.model.invoice.Invoice;
import com.pos.hyper.model.product.Product;
import com.pos.hyper.repository.InOrderRepository;
import com.pos.hyper.repository.InvoiceRepository;
import com.pos.hyper.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class InOrderService {
    private final InOrderMapper inOrderMapper;
    private final CustomExceptionHandler customExceptionHandler;
    private final InOrderRepository inOrderRepository;
    private final ProductRepository productRepository;
    private final InvoiceRepository invoiceRepository;


    public InOrderService(InOrderMapper inOrderMapper, CustomExceptionHandler customExceptionHandler, InOrderRepository inOrderRepository, ProductRepository productRepository, InvoiceRepository invoiceRepository) {
        this.inOrderMapper = inOrderMapper;
        this.customExceptionHandler = customExceptionHandler;
        this.inOrderRepository = inOrderRepository;
        this.productRepository = productRepository;
        this.invoiceRepository = invoiceRepository;
    }
    public List<InOrderDto> getAllInOrders() {
        List<InOrder> inOrders = inOrderRepository.findAll();
        return inOrders.stream().map(inOrderMapper::toInOrderDto).collect(Collectors.toList());
    }


    public InOrderDto getInOrderById(Integer id) {
        InOrder inOrder = inOrderRepository.findById(id)

                .orElseThrow(()-> customExceptionHandler
                        .handleNotFoundException("InOrder with id " + id + " not found"));

        return inOrderMapper.toInOrderDto(inOrder);
    }

    public List<InOrderDto> getAllByInvoiceId(Integer id){
        return inOrderRepository.findAllByInvoiceId(id)
                .stream()
                .map(inOrderMapper::toInOrderDto)
                .toList();
    }

    public InOrderDto createInOrder(InOrderDto inOrderDto) {

        Product product = productRepository.findById(inOrderDto.productId())
                .orElseThrow(()->customExceptionHandler
                        .handleNotFoundException("Product with id " + inOrderDto.productId() + " not found"));

        //this is for checking product quantity availability and other validation
        validateInOrder(inOrderDto,product.getQuantity(),inOrderDto.quantity());

        Invoice invoice = invoiceRepository.findById(inOrderDto.invoiceId())
                .orElseThrow(()-> customExceptionHandler
                        .handleNotFoundException(" Invoice with id " + inOrderDto.invoiceId() + " not found"));
        if(!product.getIsActive()){
            throw customExceptionHandler.handleBadRequestException("Product with id "+product.getId()+" is not active");
        }

        InOrder inOrder = inOrderMapper.toInOrder(inOrderDto, product, invoice);
        inOrder = inOrderRepository.save(inOrder);

        updateProductAndInvoice(product, invoice, inOrder.getQuantity());

        return inOrderMapper.toInOrderDto(inOrder);
    }
    public List<InOrderDto> createInOrders(List<InOrderDto> inOrderDtos) {


        List<Integer> productIds = inOrderDtos.stream()
                .map(InOrderDto::productId)
                .distinct()
                .collect(Collectors.toList());

        Map<Integer, Product> productMap = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));


        List<InOrder> invoiceItems = new ArrayList<>();
        Invoice invoice = invoiceRepository.findById(inOrderDtos.getFirst().invoiceId()).orElseThrow(()-> customExceptionHandler.handleNotFoundException("Invoice with id " + inOrderDtos.get(0).invoiceId() + " not found"));

        for (InOrderDto dto : inOrderDtos) {
            Product product = productMap.get(dto.productId());

            if (product == null || !product.getIsActive()) {
                throw new RuntimeException("Invalid product: " + dto.productId());
            }
            if (product.getQuantity() < dto.quantity()) {
                throw new RuntimeException("Insufficient stock for product: " + dto.productId());
            }

            InOrder item = new InOrder();
            if (product.getDiscount() == 0 ) {
                item.setAmount(product.getPrice() * dto.quantity());
                System.out.println("+++++++++++ set without discount +++++++++++++" + item.getAmount()+"----"+product.getDiscount());
            }else{
                // Calculate the discounted price
                item.setAmount((product.getPrice() * dto.quantity()) * (100 - product.getDiscount()) / 100);
                System.out.println("+++++++++++ set with discount +++++++++++++"+ item.getAmount()+"----"+product.getDiscount()+"-----"+product.getPrice() * dto.quantity() * (1 - product.getDiscount() / 100));
            }

            validateInOrder(dto,product.getQuantity(),dto.quantity());

            item.setProduct(product);
            item.setQuantity(dto.quantity());
            item.setUnitPrice(product.getPrice());
            item.setInvoice(invoice);
            item.setDiscount(product.getDiscount());
            item.setCostPrice(product.getCost());

            invoiceItems.add(item);

            // Update stock in-memory
            product.setQuantity(product.getQuantity() - dto.quantity());
        }

// Save all invoice items in one go
        List<InOrder> savedInOrders = inOrderRepository.saveAll(invoiceItems);

// Save updated products and invoice
         productRepository.saveAll(productMap.values());
         invoice.setTotal( inOrderRepository.getTotalByInvoice(invoice.getId()));
         invoiceRepository.save(invoice);

        return savedInOrders.stream().map(inOrderMapper::toInOrderDto).collect(Collectors.toList());



//        for (InOrderDto inOrderDto : inOrderDtos) {
//            Product product = productRepository.findById(inOrderDto.productId())
//                    .orElseThrow(() -> customExceptionHandler
//                            .handleNotFoundException("Product with id " + inOrderDto.productId() + " not found"));
//            Invoice invoice = invoiceRepository.findById(inOrderDto.invoiceId())
//                    .orElseThrow(() -> customExceptionHandler
//                            .handleNotFoundException(" Invoice with id " + inOrderDto.invoiceId() + " not found"));
//            if (!product.getIsActive()) {
//                throw customExceptionHandler.handleBadRequestException("Product with id " + product.getId() + " is not active");
//            }
//            InOrder inOrder = inOrderMapper.toInOrder(inOrderDto, product, invoice);
//            inOrders.add(inOrder);
//        }
//        inOrders = inOrderRepository.saveAll(inOrders);
//

    }


    @Transactional
    public InOrderDto updateInOrder(Integer id, InOrderDto inOrderDto) {

        Product product = productRepository.findById(inOrderDto.productId())
                .orElseThrow(()->customExceptionHandler
                        .handleNotFoundException("Product with id " + inOrderDto.productId() + " not found"));
        InOrder oldInOrder = inOrderRepository.findById(id)
                .orElseThrow(()-> customExceptionHandler
                        .handleNotFoundException("InOrder with id " + id + " not found"));

        //this is for checking product quantity availability and other validation
        validateInOrder(inOrderDto,product.getQuantity(),oldInOrder.getQuantity() - inOrderDto.quantity());

        Invoice invoice = invoiceRepository.findById(inOrderDto.invoiceId())
                .orElseThrow(()-> customExceptionHandler
                        .handleNotFoundException(" Invoice with id " + inOrderDto.invoiceId() + " not found"));


        InOrder inOrder = inOrderMapper.toInOrder(inOrderDto, product, invoice);
        inOrder.setId(id);

        inOrder = inOrderRepository.save(inOrder);

        updateProductAndInvoice(product, invoice, oldInOrder.getQuantity() - inOrder.getQuantity());

        return inOrderMapper.toInOrderDto(inOrder);
    }

    public void deleteInOrder(Integer id) {
        InOrder inOrder = inOrderRepository.findById(id)
                .orElseThrow(() -> customExceptionHandler
                        .handleNotFoundException("InOrder with id " + id + " not found"));

        inOrderRepository.delete(inOrder);
    }



    // controlling methods

    private void updateProductAndInvoice(Product product, Invoice invoice,Double quantity){
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
        invoice.setTotal(inOrderRepository.getTotalByInvoice(invoice.getId()));
        invoiceRepository.save(invoice);
    }

    private void validateInOrder(InOrderDto inOrderDto,Double productQuantity,Double inorderQuantity) {
        List<String> errors = new ArrayList<>();
        if (inOrderDto.quantity() > productQuantity) {
            errors.add("Quantity must be less than or equal to product quantity");
        }
        if (inOrderDto.quantity() > inorderQuantity) {
            errors.add("Quantity must be less than or equal to inorder quantity");
        }
        if (inOrderDto.unitPrice() <= 0) {
            errors.add("Unit price must be greater than 0");
        }
        if (inOrderDto.discount() < 0) {
            errors.add("Discount must be greater than or equal to 0");
        }
        if (inOrderDto.costPrice() <= 0) {
            errors.add("Cost price must be greater than 0");
        }
        if (inOrderDto.amount() <= 0) {
            errors.add("Amount must be greater than 0");
        }
        if (inOrderDto.quantity() <= 0) {
            errors.add("Quantity must be greater than 0");
        }
        if (inOrderDto.discount() < 0 || inOrderDto.discount() > 100 ) {
            errors.add("Discount must be between 0 and 100");
        }
        if (inOrderDto.costPrice() <= 0) {
            errors.add("Cost price must be greater than 0");
        }
        if (inOrderDto.amount() <= 0) {
            errors.add("Amount must be greater than 0");
        }


        if (!errors.isEmpty()) {
            throw customExceptionHandler.handleBadRequestExceptionSet(errors);
        }
    }
}
