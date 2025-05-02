package com.pos.hyper.model;

import com.pos.hyper.DTO.PurchaseDto;
import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.model.grn_item.GRNItemService;
import com.pos.hyper.model.grn.GRNService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PurchaseService {
    private final GRNItemService grnItemService;
    private final GRNService grnService;
    private final CustomExceptionHandler customExceptionHandler;


    public PurchaseService(GRNItemService grnItemService, GRNService grnService, CustomExceptionHandler customExceptionHandler) {
        this.grnItemService = grnItemService;
        this.grnService = grnService;
        this.customExceptionHandler = customExceptionHandler;
    }



    public List<PurchaseDto> getStockGRN() {

        return grnService
                .getAllGRNs()
                .stream()
                .map(
                        GRNDto -> new PurchaseDto(
                                GRNDto,
                                grnItemService.getGRNItemByGRNId(GRNDto.id())
                        )
                ).toList();
    }
    public PurchaseDto createStock(PurchaseDto sIDto) {


        if(!Objects.equals(sIDto.items().getFirst().GRNId(), sIDto.grn().id())){
            throw customExceptionHandler.handleBadRequestException("GRN ID does not match "+sIDto.items().getFirst().GRNId()+"///"+ sIDto.grn().id());
        }

        return grnItemService.createStockGRN(sIDto.items());
    }



}
