package com.pos.hyper.model.product;

import com.pos.hyper.repository.CategoryRepository;
import com.pos.hyper.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }
    public List<ProductDto> getAllProducts(){
        List<Product> products = productRepository.findAll();
        return products.stream().map(productMapper::toProductDto).collect(Collectors.toList());
    }
    public ProductDto getProductById(Integer id){
        Product product = productRepository.findById(id).orElse(null);
        assert product != null;
        return productMapper.toProductDto(product);
    }
    public ProductDto createProduct(ProductDto productDto) {
        validateProduct(productDto);
        Product product = productMapper.toProduct(productDto);

        product.setQuantity(0.0);

        product.setCost(0.0);
        product.setPrice(0.0);

        product = productRepository.save(product);
        return productMapper.toProductDto(product);
    }
    public ProductDto updateProduct(Integer id, ProductDto productDto) {
        validateProduct(productDto);
        Product product = productRepository.findById(id).orElse(null);
        assert product != null;
        product = productMapper.toProduct(productDto, product);
        product = productRepository.save(product);
        return productMapper.toProductDto(product);
    }
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }
    private void validateProduct(ProductDto productDto) {
        List<String> errors = new ArrayList<>();
        if (productRepository.existsByBarcode(productDto.barcode())) {
            errors.add("Barcode already exists");
        }
        if (productRepository.existsByName(productDto.name())) {
            errors.add("Name already exists");
        }
        if (!categoryRepository.existsById(productDto.categoryId())) {
            errors.add("Category does not exist");
        }
        if (!errors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,String.join("; ", errors));
        }
    }


}
