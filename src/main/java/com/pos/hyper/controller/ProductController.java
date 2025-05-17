package com.pos.hyper.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.DTO.ProductDto;
import com.pos.hyper.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/product")
//@CrossOrigin(origins = "http://localhost:5173") // we can set the origin here for access this controller api(Tharindu Mendis)
public class ProductController {

    private final ProductService productService;
    private final CustomExceptionHandler customExceptionHandler;


    public ProductController(ProductService productService, CustomExceptionHandler customExceptionHandler) {
        this.productService = productService;
        this.customExceptionHandler = customExceptionHandler;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("")
    public ResponseEntity<?> getAllProducts() {
        return productService.getAllProducts();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/stock")
    public ResponseEntity<?> getAllProductsStock() {
        return productService.getProductStock();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/stock/{id}")
    public ResponseEntity<?> getProductStockById(@PathVariable Integer id) {
        return productService.getProductStockById(id);
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Integer id) {
        return productService.getProductById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProductWithImage(
            @RequestPart("product") String productJson,
            @RequestPart("image") MultipartFile image) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        ProductDto productDto = objectMapper.readValue(productJson, ProductDto.class);

        return productService.createProduct(productDto, image);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<?> createProduct(
            @RequestBody ProductDto productDto) {
        return productService.createProduct(productDto, null);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProductWithoutImage(
            @PathVariable Integer id,
            @RequestPart("product") String productJson,
            @RequestPart("image") MultipartFile image) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        ProductDto productDto = objectMapper.readValue(productJson, ProductDto.class);

        return productService.updateProduct(productDto, image,id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProductWithoutImage(@PathVariable Integer id, @Valid @RequestBody ProductDto productDto) {
        return productService.updateProduct(id, productDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
        return productService.deleteProduct(id);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException exp) {
        return customExceptionHandler.handleMethodArgumentNotValid(exp);
    }

}
