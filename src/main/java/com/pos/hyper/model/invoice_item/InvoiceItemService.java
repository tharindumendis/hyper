package com.pos.hyper.model.invoice_item;

import com.pos.hyper.DTO.InvoiceItemDto;
import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.DTO.SaleInvoiceDto;
import com.pos.hyper.model.PaymentMethod;
import com.pos.hyper.model.Stock.Stock;
import com.pos.hyper.model.Stock.StockService;
import com.pos.hyper.model.invoice.Invoice;
import com.pos.hyper.model.invoice.InvoiceMapper;
import com.pos.hyper.model.product.Product;
import com.pos.hyper.model.product.ProductMapper;
import com.pos.hyper.model.product.ProductService;
import com.pos.hyper.model.product.ProductStockDto;
import com.pos.hyper.repository.InvoiceItemRepository;
import com.pos.hyper.repository.InvoiceRepository;
import com.pos.hyper.repository.ProductRepository;
import com.pos.hyper.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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


    public InvoiceItemService(InvoiceItemMapper invoiceItemMapper, CustomExceptionHandler customExceptionHandler, InvoiceItemRepository invoiceItemRepository, ProductRepository productRepository, InvoiceRepository invoiceRepository, InvoiceMapper invoiceMapper, StockService stockService, StockRepository stockRepository, ProductService productService, ProductMapper productMapper) {
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
    }
    public List<InvoiceItemDto> getAllInvoiceItems() {
        List<InvoiceItem> invoiceItems = invoiceItemRepository.findAll();
        return invoiceItems.stream().map(invoiceItemMapper::toInvoiceItemDto).collect(Collectors.toList());
    }


    public InvoiceItemDto getInvoiceItemById(Integer id) {
        InvoiceItem invoiceItem = invoiceItemRepository.findById(id)

                .orElseThrow(()-> customExceptionHandler
                        .handleNotFoundException("InvoiceItem with id " + id + " not found"));

        return invoiceItemMapper.toInvoiceItemDto(invoiceItem);
    }

    public List<InvoiceItemDto> getAllByInvoiceId(Integer id){
        return invoiceItemRepository.findAllByInvoiceId(id)
                .stream()
                .map(invoiceItemMapper::toInvoiceItemDto)
                .toList();
    }

    public InvoiceItemDto createInvoiceItem(InvoiceItemDto invoiceItemDto) {

        ProductStockDto product = productRepository.fetchProductStockAndCostById(invoiceItemDto.productId());
        if (product == null){
                throw customExceptionHandler.handleNotFoundException("Product with id " + invoiceItemDto.productId() + " not found");
        }

        Invoice invoice = invoiceRepository.findById(invoiceItemDto.invoiceId())
                .orElseThrow(()-> customExceptionHandler
                        .handleNotFoundException(" Invoice with id " + invoiceItemDto.invoiceId() + " not found"));
        if(!product.isActive()){
            throw customExceptionHandler.handleBadRequestException("Product with id "+product.id()+" is not active");
        }

        InvoiceItem invoiceItem = invoiceItemMapper.toInvoiceItem(invoiceItemDto, productMapper.toProduct(product), invoice);
        invoiceItem.setCostPrice(product.cost());

        invoiceItem = invoiceItemRepository.save(invoiceItem);

        updateProductAndInvoice(productMapper.toProduct(product), invoice, invoiceItem.getQuantity());

        return invoiceItemMapper.toInvoiceItemDto(invoiceItem);
    }

    public SaleInvoiceDto createInvoiceItems(SaleInvoiceDto saleInvoiceDto) {
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

        List<InvoiceItem> invoiceItems = new ArrayList<>();
        Invoice invoice = invoiceRepository.findById(invoiceItemDtos.getFirst().invoiceId())
                .orElseThrow(()-> customExceptionHandler.handleNotFoundException("Invoice with id " + invoiceItemDtos.get(0).invoiceId() + " not found"));

        if(reqInvoice.getPaymentMethod()!= null){
                invoice.setPaymentMethod(reqInvoice.getPaymentMethod());
        }

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

            // Save all invoice items
        List<InvoiceItem> savedInvoiceItems = invoiceItemRepository.saveAll(invoiceItems);

            // Update stock in the database
        List<Double> totalCostList = stockService.updateStocks(productIdsForStocks, productQuantities);

        System.out.println(totalCostList.stream().mapToInt(Double::intValue).sum());

            // Save updated products and invoice
         productRepository.saveAll(productMap.values().stream().map(productMapper::toProduct).collect(Collectors.toList()));
         invoice.setTotal( invoiceItemRepository.getTotalByInvoice(invoice.getId()));

         invoiceRepository.save(invoice);



        return new SaleInvoiceDto(invoiceMapper.toInvoiceDto(invoice), savedInvoiceItems.stream().map(invoiceItemMapper::toInvoiceItemDto).collect(Collectors.toList()));
    }
    @Transactional
    public SaleInvoiceDto updateInvoiceItems(Integer id, List<InvoiceItemDto> invoiceItemDtos) {
        List<Integer> invoiceItemIds = invoiceItemDtos.stream()
                .map(InvoiceItemDto::id)
                .distinct()
                .toList();

        Map<Integer, InvoiceItem> invoiceItemMap = invoiceItemRepository.findAllById(invoiceItemIds).stream()
                .collect(Collectors.toMap(InvoiceItem::getId, Function.identity()));


        List<Integer> productIds = invoiceItemDtos.stream()
                .map(InvoiceItemDto::productId)
                .distinct()
                .collect(Collectors.toList());

        Map<Integer, ProductStockDto> productMap = productRepository.fetchProductStockAndCostByIds(productIds).stream()
                .collect(Collectors.toMap(ProductStockDto::id, Function.identity()));

        List<InvoiceItem> invoiceItems = new ArrayList<>();
        Invoice invoice = invoiceRepository.findById(invoiceItemDtos.getFirst().invoiceId()).orElseThrow(()-> customExceptionHandler.handleNotFoundException("Invoice with id " + invoiceItemDtos.get(0).invoiceId() + " not found"));

        for (InvoiceItemDto dto : invoiceItemDtos) {
            ProductStockDto product = productMap.get(dto.productId());
            InvoiceItem invoiceItem = invoiceItemMap.get(dto.id());

            validateInvoiceItem( dto, product.stock(), invoiceItem.getQuantity());

            System.out.println("invoiceItem.getQuantity() = " + invoiceItem.getCostPrice());
            if (product == null || !product.isActive()) {
                throw new RuntimeException("Invalid product: " + dto.productId());
            }


            if (invoiceItem.getDiscount() == 0 ) {
                invoiceItem.setAmount(invoiceItem.getUnitPrice() * dto.quantity());
                System.out.println("invoiceItem.getAmount() ============ " + invoiceItem.getAmount()+"_________"+dto.quantity());
            }else{
                // Calculate the discounted price
                invoiceItem.setAmount((invoiceItem.getUnitPrice() * dto.quantity()) * (100 - invoiceItem.discount) / 100);
            }
            stockService.updateStock(product.id(), dto.quantity());


            invoiceItem.setQuantity(dto.quantity());

            invoiceItems.add(invoiceItem);

            // Update stock in-memory
        }

        // Save all invoice items in one go
        List<InvoiceItem> savedInvoiceItems = invoiceItemRepository.saveAll(invoiceItems);

        // Save updated products and invoice
        productRepository.saveAll(productMap.values().stream().map(productMapper::toProduct).collect(Collectors.toList()));
        invoice.setTotal( invoiceItemRepository.getTotalByInvoice(invoice.getId()));
        invoiceRepository.save(invoice);
        return new SaleInvoiceDto(invoiceMapper.toInvoiceDto(invoice), savedInvoiceItems.stream().map(invoiceItemMapper::toInvoiceItemDto).collect(Collectors.toList()));
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

        updateProductAndInvoice(product, invoice, oldInvoiceItem.getQuantity() - invoiceItem.getQuantity());

        return invoiceItemMapper.toInvoiceItemDto(invoiceItem);
    }

    public void deleteInvoiceItem(Integer id) {
        InvoiceItem invoiceItem = invoiceItemRepository.findById(id)
                .orElseThrow(() -> customExceptionHandler
                        .handleNotFoundException("InvoiceItem with id " + id + " not found"));
        Invoice invoice = invoiceRepository.findById(invoiceItem.getInvoice().getId()).orElseThrow(()->
                customExceptionHandler.handleNotFoundException("invoice with id " + invoiceItem.getInvoice().getId() + " not found"));

        invoiceItemRepository.delete(invoiceItem);
        Double total = invoiceItemRepository.getTotalByInvoice(invoice.getId());
        invoice.setTotal(total);
        invoiceRepository.save(invoice);

    }



    // controlling methods

    private void updateProductAndInvoice(Product product, Invoice invoice,Double quantity){

        productRepository.save(product);
        invoice.setTotal(invoiceItemRepository.getTotalByInvoice(invoice.getId()));
        invoiceRepository.save(invoice);
        stockService.updateStock(product.getId(),quantity);
    }

    private void validateInvoiceItem(InvoiceItemDto invoiceItemDto, Double availableQty, Double invoiceItemQuantity) {
        List<String> errors = new ArrayList<>();
        if (invoiceItemDto.quantity() > availableQty) {
            errors.add("Quantity must be less than or equal to product quantity");
        }
        if (invoiceItemDto.quantity() > invoiceItemQuantity) {
            errors.add("Quantity must be less than or equal to invoiceItem quantity");
        }
        if (invoiceItemDto.unitPrice() <= 0) {
            errors.add("Unit price must be greater than 0");
        }
        if (invoiceItemDto.discount() < 0) {
            errors.add("Discount must be greater than or equal to 0");
        }
        if (invoiceItemDto.costPrice() <= 0) {
            errors.add("Cost price must be greater than 0");
        }
        if (invoiceItemDto.amount() <= 0) {
            errors.add("Amount must be greater than 0");
        }
        if (invoiceItemDto.quantity() <= 0) {
            errors.add("Quantity must be greater than 0");
        }
        if (invoiceItemDto.discount() < 0 || invoiceItemDto.discount() > 100 ) {
            errors.add("Discount must be between 0 and 100");
        }
        if (invoiceItemDto.costPrice() <= 0) {
            errors.add("Cost price must be greater than 0");
        }
        if (invoiceItemDto.amount() <= 0) {
            errors.add("Amount must be greater than 0");
        }


        if (!errors.isEmpty()) {
            throw customExceptionHandler.handleBadRequestExceptionSet(errors);
        }
    }


}
