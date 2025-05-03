package com.pos.hyper.model.Stock;

import com.pos.hyper.DTO.StockDto;
import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.model.invoiceStockConsumption.InvoiceStockConsumption;
import com.pos.hyper.repository.InvoiceStockConsumptionRepository;
import com.pos.hyper.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StockService {
    private final StockMapper stockMapper ;
    private final StockRepository stockRepository;
    private final CustomExceptionHandler customExceptionHandler;
    private final InvoiceStockConsumptionRepository iscRepository;

    public StockService(StockMapper stockMapper, StockRepository stockRepository, CustomExceptionHandler customExceptionHandler, InvoiceStockConsumptionRepository iscRepository) {
        this.stockMapper = stockMapper;
        this.stockRepository = stockRepository;
        this.customExceptionHandler = customExceptionHandler;
        this.iscRepository = iscRepository;
    }

    public StockDto getStockById(Integer id) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> customExceptionHandler.handleNotFoundException("Stock with id " + id + " not found"));
        return stockMapper.toDto(stock);
    }
    public Stock findByGrnItemId(Integer grnItemId) {
        return stockRepository.findByGrnItem_Id(grnItemId);
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
    @Transactional
    public List<InvoiceStockConsumption> updateStockForInvoice(Integer productId, Double saleQuantity) {
       List<Stock> stocks =  stockRepository.findByProductIdAndQuantityGreaterThanOrderByReceivedDateAsc(productId,0);
       List<InvoiceStockConsumption> iscList = new ArrayList<>();
        for (Stock stock : stocks) {
            if (saleQuantity <= 0) break;
            InvoiceStockConsumption isc = new InvoiceStockConsumption();
            Double deductAmount = Math.min(stock.getQuantity(), saleQuantity);

            stock.setQuantity(stock.getQuantity() - deductAmount);
            saleQuantity -= deductAmount;


            isc.setStock(stockRepository.save(stock));
            isc.setQuantity(deductAmount);
            iscList.add(isc);
        }
        return iscList;
    }
    @Transactional
    public void updateStockForInvoiceReturn(Integer productId, Integer invoiceItemId , Double returnQuantity) {
        System.out.println("in update stock method updateStockForInvoiceReturn called with productId: " + productId + ", invoiceItemId: " + invoiceItemId + ", returnQuantity: " + returnQuantity);
        if (returnQuantity <= 0) return;

        List<InvoiceStockConsumption> iscList = iscRepository.findAllByInvoiceItem_Id(invoiceItemId);
        Map<Integer, Stock> relatedStocksMap = stockRepository.findAllById(iscList.stream().map(InvoiceStockConsumption::getStock).map(Stock::getId).toList())
                .stream().collect(Collectors.toMap(Stock::getId, stock -> stock));
        List<InvoiceStockConsumption> updatedIscList = new ArrayList<>();
        List<Stock> updatedStocks = new ArrayList<>();
        for (InvoiceStockConsumption isc : iscList) {
            Stock stock = relatedStocksMap.get(isc.getStock().getId());
            if (returnQuantity <= 0) break;
            Double deductAmount = Math.min(isc.getQuantity(), returnQuantity);
            returnQuantity -= deductAmount;

            isc.setQuantity(isc.getQuantity() - deductAmount);
            stock.setQuantity(stock.getQuantity() + deductAmount);
            updatedStocks.add(stock);
            updatedIscList.add(isc);

            System.out.println("in for loop "+"Updated stock quantity: " + stock.getQuantity() + ", Updated isc quantity: " + isc.getQuantity());

        }
        stockRepository.saveAll(updatedStocks);
        iscRepository.saveAll(updatedIscList);
    }



    @Transactional
    public List<List<InvoiceStockConsumption>> updateStocksForInvoices(List<Integer> productIds, List<Double> saleQuantities) {
        if (productIds.size() != saleQuantities.size()) {
            throw new IllegalArgumentException("The number of product IDs and sale quantities must be the same.");
        }

        List<Stock> stockList = new ArrayList<>();
        List<List<InvoiceStockConsumption>> listOfISCList = new ArrayList<>();

        for (int i = 0; i < productIds.size(); i++) {

            Integer productId = productIds.get(i);
            Double saleQuantity = saleQuantities.get(i);

            List<Stock> stocks =  stockRepository.findByProductIdAndQuantityGreaterThanOrderByReceivedDateAsc(productId, 0);

            if (stocks.isEmpty()) {
                throw customExceptionHandler.handleBadRequestException("No stock available for product with ID: " + productId);
            }
            List<InvoiceStockConsumption> iscList = new ArrayList<>();
            for (Stock stock : stocks) {
                InvoiceStockConsumption isc = new InvoiceStockConsumption();

                if (saleQuantity <= 0) break;

                Double deductAmount = Math.min(stock.getQuantity(), saleQuantity);

                stock.setQuantity(stock.getQuantity() - deductAmount);
                saleQuantity -= deductAmount;

                stockList.add(stock);

                isc.setQuantity(deductAmount);
                isc.setStock(stock);
                iscList.add(isc);

            }
            listOfISCList.add(iscList);
        }

        try {
            List<Stock> savedStock = stockRepository.saveAll(stockList);
            //set stock for each isc
            return listOfISCList;

        } catch (Exception e) {
            throw customExceptionHandler.handleBadRequestException("Failed to update stock for product IDs: " + productIds);
        }
    }




















    public void deleteStock(Integer id) {
        stockRepository.deleteById(id);
    }


    public List<Stock> createStocks(List<Stock> stocks) {
        return stockRepository.saveAll(stocks);
    }

    public void updateStock(Stock stock) {
        stockRepository.save(stock);
    }
    public void updateAllStock(List<Stock> stocks) {
        stockRepository.saveAll(stocks);
    }
}
