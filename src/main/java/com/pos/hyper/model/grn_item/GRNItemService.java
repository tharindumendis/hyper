package com.pos.hyper.model.grn_item;

import com.pos.hyper.DTO.GRNItemDto;
import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.model.Stock.Stock;
import com.pos.hyper.DTO.PurchaseDto;
import com.pos.hyper.model.Stock.StockService;
import com.pos.hyper.model.grn.GRN;
import com.pos.hyper.model.grn.GRNMapper;
import com.pos.hyper.model.product.Product;
import com.pos.hyper.repository.GrnItemRepository;
import com.pos.hyper.repository.GRNRepository;
import com.pos.hyper.repository.ProductRepository;
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
public class GRNItemService {
    private final GrnItemRepository grnItemRepository;
    private final GRNRepository grnRepository;
    private final ProductRepository productRepository;
    private final GRNItemMapper grnItemMapper;
    private final CustomExceptionHandler customExceptionHandler;
    private final GRNMapper grnMapper;
    private final StockService stockService;


    public GRNItemService(GrnItemRepository grnItemRepository, GRNRepository grnRepository, ProductRepository productRepository, GRNItemMapper grnItemMapper, CustomExceptionHandler customExceptionHandler, GRNMapper grnMapper, StockService stockService) {
        this.grnItemRepository = grnItemRepository;
        this.grnRepository = grnRepository;
        this.productRepository = productRepository;
        this.grnItemMapper = grnItemMapper;
        this.customExceptionHandler = customExceptionHandler;
        this.grnMapper = grnMapper;
        this.stockService = stockService;
    }
    public ResponseEntity<?> createGRNItem(GRNItemDto grnItemDto) {
        if (grnItemDto.id() != null){
            return ResponseEntity.badRequest().body("GrnItem id must be null for create GrnItem");
        }
        validateGrn(grnItemDto);
        GRN grn = grnRepository.findById(grnItemDto.GRNId()).orElse(null);
        if(grn == null){
            return customExceptionHandler.notFoundException("Inventory with id " + grnItemDto.GRNId() + " not found");
        }
        Product product = productRepository.findById(grnItemDto.productId()).orElse(null);
        if(product == null){
            return customExceptionHandler.notFoundException("Product with id " + grnItemDto.productId() + " not found");
        }
        GRNItem grnItem = grnItemMapper.toGRNItem(grnItemDto, product, grn);

        grnItem = saveGRNAfterDiscount(grnItem);

        Stock stock = new Stock();
        stock.setProduct(product);
        stock.setQuantity(grnItem.getQuantity());
        stock.setGrnItem(grnItem);
        stock.setUnitCost(grnItem.getUnitCost());

        stockService.createStock(stock);
        productRepository.save(product);
        //update GRN total
        grn.setTotal(grnItemRepository.getTotalByGRN(grnItem.getGrn().getId()));
        grnRepository.save(grn);

        return ResponseEntity.ok(grnItemMapper.toGRNItemDto(grnItem));
    }
    public PurchaseDto createGRNItems(List<GRNItemDto> sGRNDto) {
        List<Integer> productIds = sGRNDto.stream()
                .map(GRNItemDto::productId)
                .distinct()
                .toList();
        Map<Integer, Product> productMap = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
        List<GRNItem> GRNItemItems = new ArrayList<>();

        GRN grn = grnRepository.findById(sGRNDto.getFirst().GRNId())
                .orElseThrow(()-> customExceptionHandler.handleNotFoundException("GRN with id " + sGRNDto.getFirst().GRNId() + " not found"));

        List<Stock> stocks = new ArrayList<>();
        for (GRNItemDto dto : sGRNDto) {
            Product product = productMap.get(dto.productId());

            GRNItem item = new GRNItem();
            Stock stock = new Stock();

            validateGrn(dto);

            item.setProduct(product);
            item.setQuantity(dto.quantity());
            item.setGrn(grn);
            item.setDiscount(dto.discount());
            item.setUnitCost(dto.unitCost());
            if (item.getDiscount() == 0) {
                item.setAmount(dto.unitCost() * dto.quantity());
            }else{
                item.setAmount((dto.unitCost() * dto.quantity()) * (100 - dto.discount()) / 100);
            }
            stock.setProduct(product);
            stock.setQuantity(dto.quantity());
            stock.setGrnItem(item);
            stock.setUnitCost(dto.unitCost());

            GRNItemItems.add(item);
            stocks.add(stock);
        }

        grnItemRepository.saveAll(GRNItemItems);
        stockService.createStocks(stocks);

        grn.setTotal(grnItemRepository.getTotalByGRN(grn.getId()));
        grnRepository.save(grn);


        return (new PurchaseDto(grnMapper.toGRNDto(grn), grnItemRepository.findAllByGrn_Id(grn.getId()).stream().map(grnItemMapper::toGRNItemDto).collect(Collectors.toList())));

    }
    @Transactional
    public PurchaseDto updateGRNItems(List<GRNItemDto> sGRNDto) {
        GRN grn = grnRepository.findById(sGRNDto.getFirst().GRNId())
                .orElseThrow(()-> customExceptionHandler
                        .handleNotFoundException("GRN with id " + sGRNDto.getFirst().GRNId() + " not found"));
        List<Integer> productIds = sGRNDto.stream().map(GRNItemDto::productId)
                .distinct().toList();
        List<Integer> grnItemIds = sGRNDto.stream().map(GRNItemDto::id)
                .distinct().toList();
        Map<Integer, Product> productMap = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
        Map<Integer, GRNItem> grnMap = grnItemRepository.findAllById(grnItemIds).stream()
                .collect(Collectors.toMap(GRNItem::getId, Function.identity()));

        List<Stock> stocks = new ArrayList<>();

        for (GRNItemDto dto : sGRNDto) {
            Integer id = dto.id();
            if (id == null) {
                throw customExceptionHandler.handleBadRequestException("GrnItem id must not be null for update GrnItem");
            }
            Product product = productMap.get(dto.productId());
            GRNItem oldGRNItem = grnMap.get(dto.id());
            if(product == null) {
                throw customExceptionHandler.handleNotFoundException("Product with id " + dto.productId() + " not found");
            }
            if(oldGRNItem == null) {
                throw customExceptionHandler.handleNotFoundException("GRN with id " + dto.GRNId() + " not found");
            }

            if(Objects.equals(oldGRNItem.getQuantity(), dto.quantity()) && Objects.equals(oldGRNItem.getUnitCost(), dto.unitCost())) {
                continue;
            }
            Stock stock = stockService.findByGrnItemId(oldGRNItem.getId());
            Double oldQty = oldGRNItem.getQuantity();
            Double newQty = oldQty-dto.quantity() < 0 ? 0 : oldQty - dto.quantity();
            if (!Objects.equals(newQty, oldQty)) {

                stock.setQuantity(newQty);
                //update product quantity
            }

            GRNItem grnItem = grnItemMapper.toGRNItem(dto,product, grn);
            grnItem.setId(id);
            grnItem.setQuantity(newQty);
            grnItem.setUnitCost(dto.unitCost());
            saveGRNAfterDiscount(grnItem);
            stock.setUnitCost(dto.unitCost());
            stocks.add(stock);
            //update GRN total

        }
        stockService.updateAllStock(stocks);
        grn.setTotal(grnItemRepository.getTotalByGRN(grn.getId()));
        GRN newGRN = grnRepository.save(grn);
        return (new PurchaseDto(grnMapper.toGRNDto(newGRN), grnItemRepository.findAllByGrn_Id(grn.getId()).stream().map(grnItemMapper::toGRNItemDto).collect(Collectors.toList())));
    }

    @Transactional
    public ResponseEntity<?> ReturnGRN(Integer id, GRNItemDto grnItemDto) {
        ResponseEntity<?> res = validateGrn(grnItemDto);
        if(res != null) {
            return res;
        }
        GRN grn = grnRepository.findById(grnItemDto.GRNId()).orElse(null);
        if(grn == null) {
            return  customExceptionHandler.notFoundException("Grn with id " + id + " not found");
        }
        Product product = productRepository.findById(grnItemDto.productId()).orElse(null);
        if(product == null) {
            return  customExceptionHandler.notFoundException("Product with id " + id + " not found");
        }
        GRNItem oldGRNItem = grnItemRepository.findById(id).orElse(null);
        if(oldGRNItem == null) {
            return customExceptionHandler.notFoundException("Old grnItem with id " + id + " not found");
        }

        Double oldQty = oldGRNItem.getQuantity();
        Double newQty = grnItemDto.quantity();
        if (!Objects.equals(newQty, oldQty)) {
            Stock stock = stockService.findByGrnItemId(oldGRNItem.getId());
            stock.setQuantity(newQty);
            stock.setUnitCost(grnItemDto.unitCost());
            stockService.updateStock(stock);
            //update product quantity
        }

        oldGRNItem.setUnitCost(grnItemDto.unitCost());
        GRNItem grnItem = grnItemMapper.toGRNItem(grnItemDto,product, grn);
        grnItem.setId(id);
        grnItem.setQuantity(newQty);
        grnItem = saveGRNAfterDiscount(grnItem);

        //update GRN total
        grn.setTotal(grnItemRepository.getTotalByGRN(grn.getId()));
        grnRepository.save(grn);
        return ResponseEntity.ok(grnItemMapper.toGRNItemDto(grnItem));
    }

    private GRNItem saveGRNAfterDiscount(GRNItem grnItem) {
        if(grnItem.getDiscount() == 0) {
            grnItem.setAmount(grnItem.getQuantity()* grnItem.getUnitCost());
        } else {
            grnItem.setAmount(grnItem.getQuantity()* grnItem.getUnitCost() - (grnItem.getQuantity()* grnItem.getUnitCost()* grnItem.getDiscount()/100));
        }
        grnItem = grnItemRepository.save(grnItem);
        return grnItem;
    }

    public ResponseEntity<?> getGRNItemById(Integer id) {
        GRNItem grnItem = grnItemRepository.findById(id).orElse(null);
        if(grnItem == null) {
            return  customExceptionHandler.notFoundException("GRNItem with id " + id + " not found");
        }
        return ResponseEntity.ok(grnItemMapper.toGRNItemDto(grnItem));
    }
    public ResponseEntity<?> getAllGRNItem() {
        List<GRNItem> GRNItemSet = grnItemRepository.findAll();
        return ResponseEntity.ok(GRNItemSet.stream().map(grnItemMapper::toGRNItemDto).toList());
    }
    public ResponseEntity<?> deleteGRNItem(Integer id) {
        GRNItem grnItem = grnItemRepository.findById(id).orElse(null);
        if(grnItem == null) {
            return customExceptionHandler.notFoundException("GRNItem with id " + id + " not found");
        }
        grnItemRepository.delete(grnItem);
        return ResponseEntity.ok().build();
    }
    public ResponseEntity<?> getGRNItemByGRNId(Integer id) {
        List<GRNItem> GRNItemSet = grnItemRepository.findAllByGrn_Id(id);
        return ResponseEntity.ok(GRNItemSet.stream().map(grnItemMapper::toGRNItemDto).toList());
    }



    public ResponseEntity<?> validateGrn(GRNItemDto grnItemDto) {
        List<String> errors = new ArrayList<>();

        if(grnItemDto.quantity() <= 0) {
            errors.add("Quantity must be greater than 0");
        }
        if(grnItemDto.unitCost() <= 0) {
            errors.add("Unit cost must be greater than 0");
        }
        if(grnItemDto.discount() < 0) {
            errors.add("Discount must be greater than 0");
        }
        if(grnItemDto.amount() <= 0) {
            errors.add("Amount must be greater than 0");
        }
        if(!errors.isEmpty()) {
            return customExceptionHandler.badRequestExceptionSet(errors);
        }
        return null;

    }

}
