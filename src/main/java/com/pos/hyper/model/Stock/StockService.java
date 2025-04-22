package com.pos.hyper.model.Stock;

import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockService {
    private final StockMapper stockMapper ;
    private final StockRepository stockRepository;
    private final CustomExceptionHandler customExceptionHandler;

    public StockService(StockMapper stockMapper, StockRepository stockRepository, CustomExceptionHandler customExceptionHandler) {
        this.stockMapper = stockMapper;
        this.stockRepository = stockRepository;
        this.customExceptionHandler = customExceptionHandler;
    }

    public StockDto getStockById(Integer id) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> customExceptionHandler.handleNotFoundException("Stock with id " + id + " not found"));
        return stockMapper.toDto(stock);
    }
    public StockDto createStock(StockDto stockDto) {
        Stock stock = stockMapper.toStock(stockDto);
        stock = stockRepository.save(stock);
        return stockMapper.toDto(stock);
    }
    public StockDto createStock(Stock stock) {
        stock = stockRepository.save(stock);
        return stockMapper.toDto(stock);
    }

    public void updateStock (Integer productId, Double saleQuantity) {
       List<Stock> stocks =  stockRepository.findByProductIdAndQuantityGreaterThanOrderByReceivedDateAsc(productId,0);
        for (Stock stock : stocks) {
            if (saleQuantity <= 0) break;

            Double deductAmount = Math.min(stock.getQuantity(), saleQuantity);

            stock.setQuantity(stock.getQuantity() - deductAmount);
            saleQuantity -= deductAmount;

            stockRepository.save(stock);
        }
    }
    public Double updateStocks(List<Integer> productIds, List<Double> saleQuantities) {
        if (productIds.size() != saleQuantities.size()) {
            throw new IllegalArgumentException("The number of product IDs and sale quantities must be the same.");
        }
        List<Stock> stockList = new ArrayList<>();
        Double totalCost = 0.0;
        for (int i = 0; i < productIds.size(); i++) {
            Integer productId = productIds.get(i);
            Double saleQuantity = saleQuantities.get(i);

            List<Stock> stocks =  stockRepository.findByProductIdAndQuantityGreaterThanOrderByReceivedDateAsc(productId, 0);
            for (Stock stock : stocks) {
                if (saleQuantity <= 0) break;

                Double deductAmount = Math.min(stock.getQuantity(), saleQuantity);

                stock.setQuantity(stock.getQuantity() - deductAmount);
                saleQuantity -= deductAmount;

                totalCost += deductAmount * stock.getUnitCost();
                stockList.add(stock);
            }
        }
        stockRepository.saveAll(stockList);
        return totalCost;
    }



    public void deleteStock(Integer id) {
        stockRepository.deleteById(id);
    }


    public List<Stock> createStocks(List<Stock> stocks) {
        return stockRepository.saveAll(stocks);
    }
}
