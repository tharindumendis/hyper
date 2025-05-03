package com.pos.hyper.controller;

import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.DTO.GRNItemDto;
import com.pos.hyper.model.grn_item.GRNItemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grn/item")
public class GRNItemController {

    private final GRNItemService grnItemService;
    private final CustomExceptionHandler customExceptionHandler;

    public GRNItemController(GRNItemService grnItemService, CustomExceptionHandler customExceptionHandler) {
        this.grnItemService = grnItemService;
        this.customExceptionHandler = customExceptionHandler;
    }

    @GetMapping("")
    public List<GRNItemDto> getAllGRNItems() {
        return grnItemService.getAllGRNItem();
    }
    @GetMapping("/{id}")
    public GRNItemDto getGRNItemById(@PathVariable Integer id) {
        return grnItemService.getGRNItemById(id);
    }
    @PostMapping("")
    public GRNItemDto createGRNItem(@Valid @RequestBody GRNItemDto grnItemDto) {
        return grnItemService.createGRNItem(grnItemDto);
    }
    @PutMapping("/{id}")
    public GRNItemDto returnGRNItem(@PathVariable Integer id, @Valid @RequestBody GRNItemDto grnItemDto) {
        return grnItemService.ReturnGRN(id, grnItemDto);
    }
    @DeleteMapping("/{id}")
    public void deleteGRNItem(@PathVariable Integer id) {
        grnItemService.deleteGRNItem(id);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException exp) {
        return customExceptionHandler.handleMethodArgumentNotValid(exp);
    }

}
