package com.pos.hyper.controller;

import com.pos.hyper.exception.GlobalExceptionHandler;
import com.pos.hyper.model.product.Product;
import com.pos.hyper.model.product.ProductDto;
import com.pos.hyper.model.product.ProductService;
import com.pos.hyper.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
//@CrossOrigin(origins = "http://localhost:5173") // we can set the origin here for access this controller api(Tharindu Mendis)
public class ProductController {

    private final ProductService productService;
    private final GlobalExceptionHandler globalExceptionHandler;


    public ProductController(ProductService productService, GlobalExceptionHandler globalExceptionHandler) {
        this.productService = productService;
        this.globalExceptionHandler = globalExceptionHandler;
    }

    @GetMapping("")
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }
    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable Integer id) {
        return productService.getProductById(id);
    }
    @PostMapping("")
    public ProductDto createProduct(@Valid @RequestBody ProductDto productDto) {
        return productService.createProduct(productDto);
    }
    @PutMapping("/{id}")
    public ProductDto updateProduct(@PathVariable Integer id, @Valid @RequestBody ProductDto productDto) {
        return productService.updateProduct(id, productDto);
    }
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException exp) {
        return globalExceptionHandler.handleMethodArgumentNotValid(exp);
    }

}
