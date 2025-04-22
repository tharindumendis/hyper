package com.pos.hyper.model.grn;

import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.model.Stock.Stock;
import com.pos.hyper.model.StockInventoryDto;
import com.pos.hyper.model.Stock.StockService;
import com.pos.hyper.model.inventory.Inventory;
import com.pos.hyper.model.inventory.InventoryMapper;
import com.pos.hyper.model.product.Product;
import com.pos.hyper.repository.GrnRepository;
import com.pos.hyper.repository.InventoryRepository;
import com.pos.hyper.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class GrnService {
    private final GrnRepository grnRepository;
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final GrnMapper grnMapper;
    private final CustomExceptionHandler customExceptionHandler;
    private final InventoryMapper inventoryMapper;
    private final StockService stockService;


    public GrnService(GrnRepository grnRepository, InventoryRepository inventoryRepository, ProductRepository productRepository, GrnMapper grnMapper, CustomExceptionHandler customExceptionHandler, InventoryMapper inventoryMapper, StockService stockService) {
        this.grnRepository = grnRepository;
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
        this.grnMapper = grnMapper;
        this.customExceptionHandler = customExceptionHandler;
        this.inventoryMapper = inventoryMapper;
        this.stockService = stockService;
    }
    public GrnDto createGrn(GrnDto grnDto) {
        if (grnDto.id() != null){
            throw customExceptionHandler.handleBadRequestException("Grn id must be null for create Grn");
        }
        validateGrn(grnDto);
        Inventory inventory = inventoryRepository
                .findById(grnDto.inventoryId())
                .orElseThrow(()-> customExceptionHandler
                        .handleNotFoundException("Inventory with id " + grnDto.inventoryId() + " not found"));
        Product product = productRepository.findById(grnDto.productId())
                .orElseThrow(()->customExceptionHandler
                        .handleNotFoundException("Product with id " + grnDto.productId() + " not found"));
        Grn grn = grnMapper.toGrn(grnDto, product,inventory);

        grn = saveGrnAfterDiscount(grn);

        product.setQuantity(product.getQuantity() + grn.getQuantity());
        Stock stock = new Stock();
        stock.setProduct(product);
        stock.setQuantity(grn.getQuantity());
        stock.setGrn(grn);
        stock.setUnitCost(grn.getUnitCost());

        stockService.createStock(stock);
        productRepository.save(product);
        //update inventory total
        inventory.setTotal(grnRepository.getTotalByInventory(grn.getInventory().getId()));
        inventoryRepository.save(inventory);


        return grnMapper.toGrnDto(grn);
    }
    public StockInventoryDto createStockInventory(List<GrnDto> sInventoryDto) {
        List<Integer> productIds = sInventoryDto.stream()
                .map(GrnDto::productId)
                .distinct()
                .toList();
        Map<Integer, Product> productMap = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        List<Grn> grnItems = new ArrayList<>();
        Inventory inventory = inventoryRepository.findById(sInventoryDto.getFirst().inventoryId())
                .orElseThrow(()-> customExceptionHandler.handleNotFoundException("Inventory with id " + sInventoryDto.getFirst().inventoryId() + " not found"));

        List<Stock> stocks = new ArrayList<>();
        for (GrnDto dto : sInventoryDto) {
            Product product = productMap.get(dto.productId());


            Grn item = new Grn();
            Stock stock = new Stock();
            if (dto.discount() == 0 ) {
                item.setAmount(dto.unitCost() * dto.quantity());
            }else{
                // Calculate the discounted price
                item.setAmount((dto.unitCost() * dto.quantity()) * (100 - dto.discount()) / 100);
            }

            validateGrn(dto);

            item.setProduct(product);
            item.setQuantity(dto.quantity());
            item.setInventory(inventory);
            item.setDiscount(dto.discount());
            item.setUnitCost(dto.unitCost());

            grnItems.add(item);


            product.setQuantity(product.getQuantity() + dto.quantity());

            stock.setProduct(product);
            stock.setQuantity(dto.quantity());
            stock.setGrn(item);
            stock.setUnitCost(dto.unitCost());

            stocks.add(stock);


        }

        List<Grn> savedGrnItems = grnRepository.saveAll(grnItems);

        stockService.createStocks(stocks);

        productRepository.saveAll(productMap.values());
        inventory.setTotal(grnRepository.getTotalByInventory(inventory.getId()));
        inventoryRepository.save(inventory);


        return new StockInventoryDto(inventoryMapper.toInventoryDto(inventory),savedGrnItems.stream().map(grnMapper::toGrnDto).collect(Collectors.toList()));


    }

    @Transactional
    public GrnDto updateGrn(Integer id, GrnDto grnDto) {
        validateGrn(grnDto);
        Inventory inventory = inventoryRepository
                .findById(grnDto.inventoryId())
                .orElseThrow(()-> customExceptionHandler
                        .handleNotFoundException("Inventory with id " + grnDto.inventoryId() + " not found"));
        Product product = productRepository.findById(grnDto.productId())
                .orElseThrow(()->customExceptionHandler
                        .handleNotFoundException("Product with id " + grnDto.productId() + " not found"));
        Grn oldGrn = grnRepository.findById(id)
                .orElseThrow(()->customExceptionHandler
                        .handleNotFoundException("Old grn with id " + id + " not found"));
        Double oldQty = oldGrn.getQuantity();

        Grn grn = grnMapper.toGrn(grnDto,product, inventory);
        grn.setId(id);

        grn = saveGrnAfterDiscount(grn);

        //update product quantity
        product.setQuantity(product.getQuantity() - oldQty + grn.getQuantity());
        productRepository.save(product);
        //update inventory total
        inventory.setTotal(grnRepository.getTotalByInventory(grn.getInventory().getId()));
        inventoryRepository.save(inventory);

        return grnMapper.toGrnDto(grn);
    }

    private Grn saveGrnAfterDiscount(Grn grn) {
        if(grn.getDiscount() == 0) {
            grn.setAmount(grn.getQuantity()*grn.getUnitCost());
        } else {
            grn.setAmount(grn.getQuantity()*grn.getUnitCost() - (grn.getQuantity()*grn.getUnitCost()*grn.getDiscount()/100));
        }
        grn = grnRepository.save(grn);
        return grn;
    }

    public GrnDto getGrnById(Integer id) {
        Grn grn = grnRepository.findById(id)
                .orElseThrow(() -> customExceptionHandler.handleNotFoundException("Grn with id " + id + " not found"));
        return grnMapper.toGrnDto(grn);
    }
    public List<GrnDto> getAllGrn() {
        List<Grn> grnSet = grnRepository.findAll();
        return grnSet.stream().map(grnMapper::toGrnDto).toList();
    }
    public void deleteGrn(Integer id) {
        Grn grn = grnRepository.findById(id)
                .orElseThrow(() -> customExceptionHandler.handleNotFoundException("Grn with id " + id + " not found"));
        grnRepository.delete(grn);
    }
    public List<GrnDto> getGrnByInventoryId(Integer id) {
        List<Grn> grnSet = grnRepository.findAllByInventoryId(id);
        return grnSet.stream().map(grnMapper::toGrnDto).toList();
    }



    public void validateGrn(GrnDto grnDto) {
        List<String> errors = new ArrayList<>();

        if(grnDto.quantity() <= 0) {
            errors.add("Quantity must be greater than 0");
        }
        if(grnDto.unitCost() <= 0) {
            errors.add("Unit cost must be greater than 0");
        }
        if(grnDto.discount() < 0) {
            errors.add("Discount must be greater than 0");
        }
        if(grnDto.amount() <= 0) {
            errors.add("Amount must be greater than 0");
        }
        if(!errors.isEmpty()) {
            throw customExceptionHandler.handleBadRequestExceptionSet(errors);
        }

    }

}
