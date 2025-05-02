package com.pos.hyper.model.grn_item;

import com.pos.hyper.DTO.GRNItemDto;
import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.model.Stock.Stock;
import com.pos.hyper.DTO.StockGRNDto;
import com.pos.hyper.model.Stock.StockService;
import com.pos.hyper.model.grn.GRN;
import com.pos.hyper.model.grn.GRNMapper;
import com.pos.hyper.model.product.Product;
import com.pos.hyper.repository.GrnItemRepository;
import com.pos.hyper.repository.GRNRepository;
import com.pos.hyper.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    public GRNItemDto createGRNItem(GRNItemDto grnItemDto) {
        if (grnItemDto.id() != null){
            throw customExceptionHandler.handleBadRequestException("GrnItem id must be null for create GrnItem");
        }
        validateGrn(grnItemDto);
        GRN grn = grnRepository
                .findById(grnItemDto.GRNId())
                .orElseThrow(()-> customExceptionHandler
                        .handleNotFoundException("Inventory with id " + grnItemDto.GRNId() + " not found"));
        Product product = productRepository.findById(grnItemDto.productId())
                .orElseThrow(()->customExceptionHandler
                        .handleNotFoundException("Product with id " + grnItemDto.productId() + " not found"));
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


        return grnItemMapper.toGRNItemDto(grnItem);
    }
    public StockGRNDto createStockGRN(List<GRNItemDto> sGRNDto) {
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
            if (dto.discount() == 0 ) {
                item.setAmount(dto.unitCost() * dto.quantity());
            }else{
                // Calculate the discounted price
                item.setAmount((dto.unitCost() * dto.quantity()) * (100 - dto.discount()) / 100);
            }

            validateGrn(dto);

            item.setProduct(product);
            item.setQuantity(dto.quantity());
            item.setGrn(grn);
            item.setDiscount(dto.discount());
            item.setUnitCost(dto.unitCost());

            GRNItemItems.add(item);



            stock.setProduct(product);
            stock.setQuantity(dto.quantity());
            stock.setGrnItem(item);
            stock.setUnitCost(dto.unitCost());

            stocks.add(stock);


        }

        List<GRNItem> savedGRNItemItems = grnItemRepository.saveAll(GRNItemItems);

        stockService.createStocks(stocks);

        productRepository.saveAll(productMap.values());
        grn.setTotal(grnItemRepository.getTotalByGRN(grn.getId()));
        grnRepository.save(grn);


        return new StockGRNDto(grnMapper.toGRNDto(grn), savedGRNItemItems.stream().map(grnItemMapper::toGRNItemDto).collect(Collectors.toList()));


    }

    @Transactional
    public GRNItemDto updateGRN(Integer id, GRNItemDto grnItemDto) {
        validateGrn(grnItemDto);
        GRN grn = grnRepository
                .findById(grnItemDto.GRNId())
                .orElseThrow(()-> customExceptionHandler
                        .handleNotFoundException("GRN with id " + grnItemDto.GRNId() + " not found"));
        Product product = productRepository.findById(grnItemDto.productId())
                .orElseThrow(()->customExceptionHandler
                        .handleNotFoundException("Product with id " + grnItemDto.productId() + " not found"));
        GRNItem oldGRNItem = grnItemRepository.findById(id)
                .orElseThrow(()->customExceptionHandler
                        .handleNotFoundException("Old grnItem with id " + id + " not found"));
        Double oldQty = oldGRNItem.getQuantity();

        GRNItem grnItem = grnItemMapper.toGRNItem(grnItemDto,product, grn);
        grnItem.setId(id);

        grnItem = saveGRNAfterDiscount(grnItem);

        //update product quantity
        productRepository.save(product);
        //update GRN total
        grn.setTotal(grnItemRepository.getTotalByGRN(grnItem.getGrn().getId()));
        grnRepository.save(grn);

        return grnItemMapper.toGRNItemDto(grnItem);
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

    public GRNItemDto getGRNItemById(Integer id) {
        GRNItem grnItem = grnItemRepository.findById(id)
                .orElseThrow(() -> customExceptionHandler.handleNotFoundException("GRNItem with id " + id + " not found"));
        return grnItemMapper.toGRNItemDto(grnItem);
    }
    public List<GRNItemDto> getAllGRNItem() {
        List<GRNItem> GRNItemSet = grnItemRepository.findAll();
        return GRNItemSet.stream().map(grnItemMapper::toGRNItemDto).toList();
    }
    public void deleteGRNItem(Integer id) {
        GRNItem grnItem = grnItemRepository.findById(id)
                .orElseThrow(() -> customExceptionHandler.handleNotFoundException("GRNItem with id " + id + " not found"));
        grnItemRepository.delete(grnItem);
    }
    public List<GRNItemDto> getGRNItemByGRNId(Integer id) {
        List<GRNItem> GRNItemSet = grnItemRepository.findAllByGrn_Id(id);
        return GRNItemSet.stream().map(grnItemMapper::toGRNItemDto).toList();
    }



    public void validateGrn(GRNItemDto grnItemDto) {
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
            throw customExceptionHandler.handleBadRequestExceptionSet(errors);
        }

    }

}
