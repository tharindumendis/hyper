package com.pos.hyper.controller;

import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.DTO.GRNItemDto;
import com.pos.hyper.service.GRNItemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/grn/item")
public class GRNItemController {

    private final GRNItemService grnItemService;
    private final CustomExceptionHandler customExceptionHandler;

    public GRNItemController(GRNItemService grnItemService, CustomExceptionHandler customExceptionHandler) {
        this.grnItemService = grnItemService;
        this.customExceptionHandler = customExceptionHandler;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("")
    public ResponseEntity<?> getAllGRNItems() {
        return grnItemService.getAllGRNItem();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getGRNItemById(@PathVariable Integer id) {
        return grnItemService.getGRNItemById(id);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<?> createGRNItem(@Valid @RequestBody GRNItemDto grnItemDto) {
        return grnItemService.createGRNItem(grnItemDto);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> returnGRNItem(@PathVariable Integer id, @Valid @RequestBody GRNItemDto grnItemDto) {
        return grnItemService.ReturnGRN(id, grnItemDto);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGRNItem(@PathVariable Integer id) {
        return grnItemService.deleteGRNItem(id);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException exp) {
        return customExceptionHandler.handleMethodArgumentNotValid(exp);
    }

}
