package com.pos.hyper.controller;

import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.DTO.OrgDto;
import com.pos.hyper.service.OrgService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("")
    public ResponseEntity<?> getOrg(){
        return orgService.getOrg();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<?> createOrg(@Valid @RequestBody OrgDto orgDto){
        return orgService.createOrg(orgDto);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("")
    public ResponseEntity<?> updateOrg(@Valid @RequestBody OrgDto orgDto){
        return orgService.updateOrg(orgDto);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException exp) {
        return exceptionHandler.handleMethodArgumentNotValid(exp);
    }









}
