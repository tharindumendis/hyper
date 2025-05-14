package com.pos.hyper.model;

import com.pos.hyper.DTO.PurchaseDto;
import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.model.grn.GRNMapper;
import com.pos.hyper.model.grn_item.GRNItem;
import com.pos.hyper.model.grn_item.GRNItemMapper;
import com.pos.hyper.model.grn_item.GRNItemService;
import com.pos.hyper.model.grn.GRNService;
import com.pos.hyper.repository.GRNRepository;
import com.pos.hyper.repository.GrnItemRepository;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PurchaseService {
    private final GRNItemService grnItemService;
    private final GrnItemRepository grnItemRepository;
    private final GRNItemMapper grnItemMapper;
    private final GRNRepository grnRepository;
    private final GRNMapper grnMapper;
    private final CustomExceptionHandler customExceptionHandler;


    public PurchaseService(GRNItemService grnItemService, GrnItemRepository grnItemRepository, GRNItemMapper grnItemMapper, GRNRepository grnRepository, GRNMapper grnMapper, CustomExceptionHandler customExceptionHandler) {
        this.grnItemService = grnItemService;
        this.grnItemRepository = grnItemRepository;
        this.grnItemMapper = grnItemMapper;
        this.grnRepository = grnRepository;
        this.grnMapper = grnMapper;
        this.customExceptionHandler = customExceptionHandler;
    }



    public ResponseEntity<?> getStockGRN() {

        return ResponseEntity.ok(grnRepository.findAll()
                .stream()
                .map(
                        GRN -> new PurchaseDto(
                                grnMapper.toGRNDto(GRN),
                                grnItemRepository.findAllByGrn_Id(GRN.getId()).stream().map(grnItemMapper::toGRNItemDto).toList()
                        )
                ).toList());
    }
    public ResponseEntity<?> createPurchase(PurchaseDto sIDto) {


        if(!Objects.equals(sIDto.items().getFirst().GRNId(), sIDto.grn().id())){
            throw customExceptionHandler.handleBadRequestException("GRN ID does not match "+sIDto.items().getFirst().GRNId()+"///"+ sIDto.grn().id());
        }
        return ResponseEntity.ok(grnItemService.createGRNItems(sIDto.items()));
    }


    public ResponseEntity<?> returnPurchase(Integer id, PurchaseDto sIDto) {
        if(!Objects.equals(sIDto.items().getFirst().GRNId(), sIDto.grn().id())){
            throw customExceptionHandler.handleBadRequestException("GRN ID does not match "+sIDto.items().getFirst().GRNId()+"///"+ sIDto.grn().id());
        }
        return ResponseEntity.ok(grnItemService.updateGRNItems( sIDto.items()));
    }

    public ResponseEntity<?> getPurchaseById(Integer id) {
        return ResponseEntity.ok(grnRepository.findById(id)
                .map(
                        GRN -> new PurchaseDto(
                                grnMapper.toGRNDto(GRN),
                                grnItemRepository.findAllByGrn_Id(GRN.getId()).stream().map(grnItemMapper::toGRNItemDto).toList()
                        )
                ).orElseThrow(() -> customExceptionHandler.handleNotFoundException("Purchase not found with id: " + id)));
    }
}
