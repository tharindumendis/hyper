package com.pos.hyper.model.product;

import com.pos.hyper.DTO.ProductDto;
import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.repository.CategoryRepository;
import com.pos.hyper.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
    public ProductDto createProduct(ProductDto productDto, MultipartFile file) {
        validateProduct(productDto,"create");
        Product product = productMapper.toProduct(productDto);
        product.setImage(null);
        product = productRepository.save(product);
        if(file != null){
            return uploadImage(file, product);
        }
        return productMapper.toProductDto(product);

    }

    public ProductDto updateProduct(ProductDto productDto, MultipartFile file, Integer id) {
        validateProduct(productDto,"update");
        Product product = productRepository.findById(id)
                .orElseThrow(()-> customExceptionHandler
                        .handleNotFoundException("Product with id " + id + " not found"));
        product = productMapper.toProduct(productDto, product);
        product.setId(id);

        return uploadImage(file, product);
    }

    private ProductDto uploadImage(MultipartFile file, Product product) {
        product.setImage(product.getId()+".jpg");
        try {

            File uploadDir = new File("uploads/");
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String fileName = product.getId() + ".jpg";
            Path path = Paths.get("uploads/" + fileName);
            System.out.println("Path: " + path);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Image uploaded successfully.");

            String imageUrl = "api/image/" + product.getId();
            product.setImage(imageUrl);
            productRepository.save(product);
            ;
        } catch (IOException e) {
            System.err.println("Error uploading image: " + product.getId() + ".jpg");
            e.printStackTrace();
        }


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

    public List<ProductStockDto> getProductStock() {
        return productRepository.fetchProductStockAndCost();

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


    public ProductStockDto getProductStockById(Integer id) {
        return productRepository.fetchProductStockAndCostById(id);
    }
}
