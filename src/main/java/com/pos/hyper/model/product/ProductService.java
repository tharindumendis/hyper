package com.pos.hyper.model.product;

import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.repository.CategoryRepository;
import com.pos.hyper.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final CustomExceptionHandler customExceptionHandler;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, ProductMapper productMapper, CustomExceptionHandler customExceptionHandler) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
        this.customExceptionHandler = customExceptionHandler;
    }
    public List<ProductDto> getAllProducts(){
        List<Product> products = productRepository.findAll();
        return products.stream().map(productMapper::toProductDto).collect(Collectors.toList());
    }
    public ProductDto getProductById(Integer id){
        Product product = productRepository.findById(id)
                .orElseThrow(()-> customExceptionHandler
                        .handleNotFoundException("Product with id " + id + " not found"));
        return productMapper.toProductDto(product);
    }
    public ProductDto createProduct(ProductDto productDto) {
        validateProduct(productDto,"create");
        Product product = productMapper.toProduct(productDto);
        product.setQuantity(0.0);
        product.setCost(0.0);
        product.setPrice(0.0);
        product = productRepository.save(product);
        return productMapper.toProductDto(product);
    }
    @Transactional
    public ProductDto updateProduct(Integer id, ProductDto productDto) {
        validateProduct(productDto,"update");
        Product product = productRepository.findById(id)
                .orElseThrow(()-> customExceptionHandler
                        .handleNotFoundException("Product with id " + id + " not found"));
        product = productMapper.toProduct(productDto, product);
        product.setId(id);
        product = productRepository.save(product);
        return productMapper.toProductDto(product);
    }
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }

    private void validateProduct(ProductDto productDto,String method) {
        List<String> errors = new ArrayList<>();
        if (!method.equals("update")){
            if (productRepository.existsByBarcode(productDto.barcode())) {
                errors.add("Barcode already exists");
            }
            if (productRepository.existsByName(productDto.name())) {
                errors.add("Name already exists");
            }
        }

        if (!categoryRepository.existsById(productDto.categoryId())) {
            errors.add("Category does not exist");
        }
        if (!errors.isEmpty()) {
            throw customExceptionHandler.handleBadRequestExceptionSet(errors);
        }
    }


}
