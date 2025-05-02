package com.pos.hyper.model;

import com.pos.hyper.DTO.StockGRNDto;
import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.model.grn_item.GRNItemService;
import com.pos.hyper.model.grn.GRNService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class StockGRNService {
    private final GRNItemService grnItemService;
    private final GRNService grnService;
    private final CustomExceptionHandler customExceptionHandler;


    public StockGRNService(GRNItemService grnItemService, GRNService grnService, CustomExceptionHandler customExceptionHandler) {
        this.grnItemService = grnItemService;
        this.grnService = grnService;
        this.customExceptionHandler = customExceptionHandler;
    }



    public List<StockGRNDto> getStockGRN() {

        return grnService
                .getAllGRNs()
                .stream()
                .map(
                        GRNDto -> new StockGRNDto(
                                GRNDto,
                                grnItemService.getGRNItemByGRNId(GRNDto.id())
                        )
                ).toList();
    }
    public StockGRNDto createStock(StockGRNDto sIDto) {


        if(!Objects.equals(sIDto.items().getFirst().GRNId(), sIDto.grn().id())){
            throw customExceptionHandler.handleBadRequestException("GRN ID does not match "+sIDto.items().getFirst().GRNId()+"///"+ sIDto.grn().id());
        }

        return grnItemService.createStockGRN(sIDto.items());
    }



}
