package com.pos.hyper.model.invoice_item;

import com.pos.hyper.DTO.InvoiceItemDto;
import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.DTO.SaleInvoiceDto;
import com.pos.hyper.model.PaymentMethod;
import com.pos.hyper.model.Stock.StockService;
import com.pos.hyper.model.customer.Customer;
import com.pos.hyper.model.invoice.Invoice;
import com.pos.hyper.model.invoice.InvoiceMapper;
import com.pos.hyper.model.invoiceStockConsumption.InvoiceStockConsumption;
import com.pos.hyper.model.invoiceStockConsumption.InvoiceStockConsumptionService;
import com.pos.hyper.model.product.Product;
import com.pos.hyper.model.product.ProductMapper;
import com.pos.hyper.model.product.ProductService;
import com.pos.hyper.model.product.ProductStockDto;
import com.pos.hyper.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class InvoiceItemService {
    private final InvoiceItemMapper invoiceItemMapper;
    private final CustomExceptionHandler customExceptionHandler;
    private final InvoiceItemRepository invoiceItemRepository;
    private final ProductRepository productRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;
    private final StockService stockService;
    private final StockRepository stockRepository;
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final InvoiceStockConsumptionService iscService;
    private final CustomerRepository customerRepository;


    public InvoiceItemService(InvoiceItemMapper invoiceItemMapper, CustomExceptionHandler customExceptionHandler, InvoiceItemRepository invoiceItemRepository, ProductRepository productRepository, InvoiceRepository invoiceRepository, InvoiceMapper invoiceMapper, StockService stockService, StockRepository stockRepository, ProductService productService, ProductMapper productMapper, InvoiceStockConsumptionService invoiceStockConsumptionService, CustomerRepository customerRepository) {
        this.invoiceItemMapper = invoiceItemMapper;
        this.customExceptionHandler = customExceptionHandler;
        this.invoiceItemRepository = invoiceItemRepository;
        this.productRepository = productRepository;
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
        this.stockService = stockService;
        this.stockRepository = stockRepository;
        this.productService = productService;
        this.productMapper = productMapper;
        this.iscService = invoiceStockConsumptionService;
        this.customerRepository = customerRepository;
    }
    public ResponseEntity<?> getAllInvoiceItems() {
        List<InvoiceItem> invoiceItems = invoiceItemRepository.findAll();
        return ResponseEntity.ok(invoiceItems.stream().map(invoiceItemMapper::toInvoiceItemDto).collect(Collectors.toList()));
    }


    public ResponseEntity<?> getInvoiceItemById(Integer id) {
        InvoiceItem invoiceItem = invoiceItemRepository.findById(id)

                .orElseThrow(()-> customExceptionHandler
                        .handleNotFoundException("InvoiceItem with id " + id + " not found"));

        return ResponseEntity.ok(invoiceItemMapper.toInvoiceItemDto(invoiceItem));
    }

    public List<InvoiceItemDto> getAllByInvoiceId(Integer id){
        return invoiceItemRepository.findAllByInvoiceId(id)
                .stream()
                .map(invoiceItemMapper::toInvoiceItemDto)
                .toList();
    }

    public ResponseEntity<?> createInvoiceItem(InvoiceItemDto invoiceItemDto) {

        ProductStockDto product = productRepository.fetchProductStockAndCostById(invoiceItemDto.productId());
        if (product == null){
                return customExceptionHandler.notFoundException("Product with id " + invoiceItemDto.productId() + " not found");
        }

        Invoice invoice = invoiceRepository.findById(invoiceItemDto.invoiceId()).orElse(null);
        if(invoice == null){
            return customExceptionHandler.notFoundException("Invoice with id " + invoiceItemDto.invoiceId() + " not found");
        }
        if(!product.isActive()){
            return customExceptionHandler.badRequestException("Product with id "+product.id()+" is not active");
        }

        InvoiceItem invoiceItem = invoiceItemMapper.toInvoiceItem(invoiceItemDto, productMapper.toProduct(product), invoice);
        invoiceItem.setCostPrice(product.cost());
        invoiceItem.setQuantity(invoiceItemDto.quantity());
        invoiceItem.setUnitPrice(product.price());
        invoiceItem.setInvoice(invoice);
        invoiceItem.setDiscount(product.discount());
        invoiceItem.setCostPrice(product.cost());

        invoiceItem = invoiceItemRepository.save(invoiceItem);

        updateProductAndInvoice(productMapper.toProduct(product), invoice, invoiceItem.getQuantity());

        return ResponseEntity.ok(invoiceItemMapper.toInvoiceItemDto(invoiceItem));
    }

    public ResponseEntity<?> createInvoiceItems(SaleInvoiceDto saleInvoiceDto) {
        List<InvoiceItemDto> invoiceItemDtos = saleInvoiceDto.items();
        Invoice reqInvoice = new Invoice();
        reqInvoice.setId(saleInvoiceDto.invoice().id());
        try {
            reqInvoice.setPaymentMethod(PaymentMethod.valueOf(saleInvoiceDto.invoice().paymentMethod()));
        }catch (IllegalArgumentException e) {
            reqInvoice.setPaymentMethod(null);
        }

        List<Integer> productIds = invoiceItemDtos.stream()
                .map(InvoiceItemDto::productId)
                .distinct()
                .collect(Collectors.toList());

        Map<Integer, ProductStockDto> productMap = productRepository.fetchProductStockAndCostByIds(productIds).stream()
                .collect(Collectors.toMap(ProductStockDto::id, Function.identity()));



        Invoice invoice = invoiceRepository.findById(invoiceItemDtos.getFirst().invoiceId()).orElse(null);
        if(invoice == null){
            return customExceptionHandler.notFoundException("Invoice with id " + invoiceItemDtos.getFirst().invoiceId() + " not found");
        }

        if(reqInvoice.getPaymentMethod()!= null){
                invoice.setPaymentMethod(reqInvoice.getPaymentMethod());
        }

        List<InvoiceItem> invoiceItems = new ArrayList<>();
        List<List<InvoiceStockConsumption>> listOfISCList;
        List<Integer> productIdsForStocks = new ArrayList<>();
        List<Double> productQuantities = new ArrayList<>();


        for (InvoiceItemDto dto : invoiceItemDtos) {
            ProductStockDto product = productMap.get(dto.productId());

            if (product == null || !product.isActive()) {
                throw new RuntimeException("Invalid product: " + dto.productId());
            }

            InvoiceItem item = new InvoiceItem();

            if (product.discount() == 0 ) {
                item.setAmount(product.price() * dto.quantity());
            }else{
                // Calculate the discounted price
                item.setAmount((product.price() * dto.quantity()) * (100 - product.discount()) / 100);
            }


            item.setProduct(productMapper.toProduct(product));
            item.setQuantity(dto.quantity());
            item.setUnitPrice(product.price());
            item.setInvoice(invoice);
            item.setDiscount(product.discount());
            item.setCostPrice(product.cost());

            invoiceItems.add(item);

            // Update stock in-memory
            productIdsForStocks.add(product.id());
            productQuantities.add(dto.quantity());


        }

        listOfISCList = stockService.updateStocksForInvoices(productIdsForStocks, productQuantities);



        // Save all invoice items
        List<InvoiceItem> savedInvoiceItems = invoiceItemRepository.saveAll(invoiceItems);
        for (InvoiceItem item : savedInvoiceItems) {
            int index = savedInvoiceItems.indexOf(item);
            List<InvoiceStockConsumption> iscList = listOfISCList.get(index);
            iscList.forEach(isc -> isc.setInvoiceItem(item));
            iscService.createAllISCs(iscList);
        }

         invoice.setTotal( invoiceItemRepository.getTotalByInvoice(invoice.getId()));
        if(saleInvoiceDto.invoice().customerId() != null){
            Customer customer = customerRepository.findById(saleInvoiceDto.invoice().customerId()).orElse(null);
            invoice.setCustomer(customer);
        }
         invoiceRepository.save(invoice);



        return ResponseEntity.ok(new SaleInvoiceDto(invoiceMapper.toInvoiceDto(invoice), invoiceItemRepository.findAllByInvoiceId(invoice.getId()).stream().map(invoiceItemMapper::toInvoiceItemDto).collect(Collectors.toList())));
    }

    @Transactional
    public SaleInvoiceDto returnInvoiceItems(Integer id, List<InvoiceItemDto> invoiceItemDtos) {

        List<Integer> invoiceItemIds = invoiceItemDtos.stream()
                .map(InvoiceItemDto::id)
                .distinct()
                .toList();
        List<Integer> productIds = invoiceItemDtos.stream()
                .map(InvoiceItemDto::productId)
                .distinct()
                .collect(Collectors.toList());


        Map<Integer, InvoiceItem> invoiceItemMap = invoiceItemRepository.findAllById(invoiceItemIds).stream()
                .collect(Collectors.toMap(InvoiceItem::getId, Function.identity()));

        Map<Integer, ProductStockDto> productMap = productRepository.fetchProductStockAndCostByIds(productIds).stream()
                .collect(Collectors.toMap(ProductStockDto::id, Function.identity()));
        Invoice invoice = invoiceRepository.findById(invoiceItemDtos.getFirst().invoiceId())
                .orElseThrow(()-> customExceptionHandler
                        .handleNotFoundException("Invoice with id " + invoiceItemDtos.getFirst().invoiceId() + " not found"));

        List<InvoiceItem> invoiceItems = new ArrayList<>();

        for (InvoiceItemDto dto : invoiceItemDtos){
            InvoiceItem invoiceItem = invoiceItemMap.get(dto.id());
            if(invoiceItem == null || invoiceItem.getQuantity() < dto.quantity()){
                throw customExceptionHandler.handleBadRequestException("Invoice item with id " + dto.id() + " not found or quantity is invalid");
            }
        }

        for (InvoiceItemDto dto : invoiceItemDtos) {
            ProductStockDto product = productMap.get(dto.productId());
            InvoiceItem invoiceItem = invoiceItemMap.get(dto.id());
            if(Objects.equals(invoiceItem.getQuantity(), dto.quantity())){
                System.out.println("Invoice item with id " + dto.id() + " has no return quantity");
                continue;
            }
            System.out.println("Invoice item with id " + dto.id() + " has return quantity");

            validateInvoiceItem( dto, product.stock(), invoiceItem.getQuantity(), "return");

            // Calculate the discounted price
            if (invoiceItem.getDiscount() == 0 ) {
                invoiceItem.setAmount(invoiceItem.getUnitPrice() * invoiceItem.getQuantity()-dto.quantity());
            }else{
                invoiceItem.setAmount((invoiceItem.getUnitPrice() * invoiceItem.getQuantity()-dto.quantity()) * (100 - invoiceItem.discount) / 100);
            }

            // Update stock in-memory
            stockService.updateStockForInvoiceReturn(product.id(), invoiceItem.getId(),dto.quantity() );

            // Update invoice item
            invoiceItem.setQuantity(invoiceItem.getQuantity()-dto.quantity());

            invoiceItems.add(invoiceItem);

        }

        // Save all invoice items in one go
        invoiceItemRepository.saveAll(invoiceItems);
        invoice.setTotal( invoiceItemRepository.getTotalByInvoice(invoice.getId()));
        invoiceRepository.save(invoice);
        return new SaleInvoiceDto(invoiceMapper.toInvoiceDto(invoice), invoiceItemRepository.findAllByInvoiceId(invoice.getId()).stream().map(invoiceItemMapper::toInvoiceItemDto).collect(Collectors.toList()));
    }

    @Transactional
    public InvoiceItemDto updateInvoiceItem(Integer id, InvoiceItemDto invoiceItemDto) {

        Product product = productRepository.findById(invoiceItemDto.productId())
                .orElseThrow(()->customExceptionHandler
                        .handleNotFoundException("Product with id " + invoiceItemDto.productId() + " not found"));
        InvoiceItem oldInvoiceItem = invoiceItemRepository.findById(id)
                .orElseThrow(()-> customExceptionHandler
                        .handleNotFoundException("InvoiceItem with id " + id + " not found"));

        //this is for checking product quantity availability and other validation

        Invoice invoice = invoiceRepository.findById(invoiceItemDto.invoiceId())
                .orElseThrow(()-> customExceptionHandler
                        .handleNotFoundException(" Invoice with id " + invoiceItemDto.invoiceId() + " not found"));


        InvoiceItem invoiceItem = invoiceItemMapper.toInvoiceItem(invoiceItemDto, product, invoice);
        invoiceItem.setId(id);

        invoiceItem = invoiceItemRepository.save(invoiceItem);

        //have to implement stock return logic
        updateProductAndInvoice(product, invoice, oldInvoiceItem.getQuantity() - invoiceItem.getQuantity());

        return invoiceItemMapper.toInvoiceItemDto(invoiceItem);
    }

    public ResponseEntity<?> deleteInvoiceItem(Integer id) {
        InvoiceItem invoiceItem = invoiceItemRepository.findById(id).orElse(null);
        if (invoiceItem == null) {
            return customExceptionHandler.notFoundException("InvoiceItem with id " + id + " not found");
        }
        Invoice invoice = invoiceRepository.findById(invoiceItem.getInvoice().getId()).orElse(null);
        if (invoice == null) {
            return customExceptionHandler.notFoundException("invoice with id " + id + " not found");
        }
        invoiceItemRepository.delete(invoiceItem);
        Double total = invoiceItemRepository.getTotalByInvoice(invoice.getId());
        invoice.setTotal(total);
        invoiceRepository.save(invoice);
        return ResponseEntity.ok().build();
    }



    // controlling methods

    private void updateProductAndInvoice(Product product, Invoice invoice,Double quantity){

        productRepository.save(product);
        invoice.setTotal(invoiceItemRepository.getTotalByInvoice(invoice.getId()));
        invoiceRepository.save(invoice);
        stockService.updateStockForInvoice(product.getId(),quantity);
    }

    private void validateInvoiceItem(InvoiceItemDto invoiceItemDto, Double availableQty, Double invoiceItemQuantity, String type) {
        List<String> errors = new ArrayList<>();

        if (invoiceItemDto.quantity() > invoiceItemQuantity) {
            errors.add("Quantity must be less than or equal to invoiceItem quantity");
        }
        if (invoiceItemDto.unitPrice() <= 0) {
            errors.add("Unit price must be greater than 0");
        }
        if (invoiceItemDto.discount() < 0) {
            errors.add("Discount must be greater than or equal to 0");
        }

        if (invoiceItemDto.quantity() < 0) {
                errors.add("Return Quantity must be greater than or equal to 0");

        }
        if (invoiceItemDto.discount() < 0 || invoiceItemDto.discount() > 100 ) {
            errors.add("Discount must be between 0 and 100");
        }


        if (!errors.isEmpty()) {
            throw  customExceptionHandler.handleBadRequestExceptionSet(errors);
        }
    }


}
