package com.pos.hyper.controller;

import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.DTO.OrgDto;
import com.pos.hyper.service.OrgService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@EnableMethodSecurity
@RestController
@RequestMapping("/api/org")
public class OrgController {
    private final OrgService orgService;
    private final CustomExceptionHandler exceptionHandler;

    public OrgController(OrgService orgService, CustomExceptionHandler exceptionHandler) {
        this.orgService = orgService;
        this.exceptionHandler = exceptionHandler;
    }

    @GetMapping("")
    public ResponseEntity<?> getOrg(){
        return orgService.getOrg();
    }
    @PostMapping("")
    public ResponseEntity<?> createOrg(@Valid @RequestBody OrgDto orgDto){
        return orgService.createOrg(orgDto);
    }
    @PutMapping("")
    public ResponseEntity<?> updateOrg(@Valid @RequestBody OrgDto orgDto){
        return orgService.updateOrg(orgDto);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException exp) {
        return exceptionHandler.handleMethodArgumentNotValid(exp);
    }









}
