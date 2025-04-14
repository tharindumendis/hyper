package com.pos.hyper.controller;

import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.model.grn.GrnDto;
import com.pos.hyper.model.grn.GrnService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grn")
public class GrnController {

    private final GrnService grnService;
    private final CustomExceptionHandler customExceptionHandler;


    public GrnController(GrnService grnService, CustomExceptionHandler customExceptionHandler) {
        this.grnService = grnService;
        this.customExceptionHandler = customExceptionHandler;
    }
    @GetMapping("")
    public List<GrnDto> getAllGrn() {
        return grnService.getAllGrn();
    }
    @GetMapping("/{id}")
    public GrnDto getGrnById(@PathVariable Integer id) {
        return grnService.getGrnById(id);
    }
    @PostMapping("")
    public GrnDto createGrn(@Valid @RequestBody GrnDto grnDto) {
        return grnService.createGrn(grnDto);
    }
    @PutMapping("/{id}")
    public GrnDto updateGrn(@PathVariable Integer id, @Valid @RequestBody GrnDto grnDto) {
        return grnService.updateGrn(id, grnDto);
    }
    @DeleteMapping("/{id}")
    public void deleteGrn(@PathVariable Integer id) {
        grnService.deleteGrn(id);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException exp) {
        return customExceptionHandler.handleMethodArgumentNotValid(exp);
    }

}
