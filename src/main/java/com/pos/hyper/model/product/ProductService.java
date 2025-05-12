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
    public ResponseEntity<?> getAllProducts(){
        List<Product> products = productRepository.findAll();
        return ResponseEntity.ok(products.stream().map(productMapper::toProductDto).collect(Collectors.toList()));
    }
    public ResponseEntity<?> getProductById(Integer id){
        Product product = productRepository.findById(id).orElse(null);
        if(product == null){
            return customExceptionHandler.notFoundException("Product with id " + id + " not found");
        }
        return ResponseEntity.ok(productMapper.toProductDto(product));
    }
    public ResponseEntity<?> getProductByIdForBill(Integer id){
        Product product = productRepository.findById(id).orElse(null);
        if(product == null){
            return customExceptionHandler.notFoundException("Product with id " + id + " not found");
        }
        product.setPrice(null);
        product.setCreatedAt(null);
        product.setUpdatedAt(null);
        product.setBarcode(null);
        product.setDescription(null);
        product.setDiscount(null);
        product.setImage(null);
        return ResponseEntity.ok(productMapper.toProductBillDto(product));
    }
    public ResponseEntity<?> createProduct(ProductDto productDto, MultipartFile file) {
        ResponseEntity<?> rs =validateProduct(productDto,"create");
        if(rs != null){
            return rs;
        }
        Product product = productMapper.toProduct(productDto);
        product.setImage(null);
        product = productRepository.save(product);
        if(file != null){
            return ResponseEntity.ok(uploadImage(file, product));
        }
        return ResponseEntity.ok(productMapper.toProductDto(product));

    }

    public ResponseEntity<?> updateProduct(ProductDto productDto, MultipartFile file, Integer id) {
        ResponseEntity<?> rs = validateProduct(productDto,"update");
        if(rs != null){
            return rs;
        }
        Product product = productRepository.findById(id).orElse(null);
        if(product == null){
            return customExceptionHandler.notFoundException("Product with id " + id + " not found");
        }
        product = productMapper.toProduct(productDto, product);
        product.setId(id);

        return ResponseEntity.ok(uploadImage(file, product));
    }

    public ResponseEntity<?> uploadImage(MultipartFile file, Product product) {
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

            String imageUrl = "api/image/product/" + product.getId();
            product.setImage(imageUrl);
            productRepository.save(product);
            ;
        } catch (IOException e) {
            System.err.println("Error uploading image: " + product.getId() + ".jpg");
            e.printStackTrace();
            throw new RuntimeException("Error uploading image: " + product.getId() + ".jpg");
        }
        return ResponseEntity.ok(productMapper.toProductDto(product));
    }


    @Transactional
    public ResponseEntity<?> updateProduct(Integer id, ProductDto productDto) {
        ResponseEntity<?> rs = validateProduct(productDto,"update");
        if(rs != null){
            return rs;
        }
        Product product = productRepository.findById(id).orElse(null);
        if(product == null){
            return customExceptionHandler.notFoundException("Product with id " + id + " not found");
        }
        product = productMapper.toProduct(productDto, product);
        product.setId(id);
        product = productRepository.save(product);
        return ResponseEntity.ok(productMapper.toProductDto(product));
    }

    public ResponseEntity<?> deleteProduct(Integer id) {
        productRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> getProductStock() {
        Object productStock = productRepository.fetchProductStockAndCost();
        if(productStock == null){
            return customExceptionHandler.notFoundException("No product found");
        }
        return ResponseEntity.ok(productStock);

    }

    private ResponseEntity<?> validateProduct(ProductDto productDto,String method) {
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
            return customExceptionHandler.badRequestExceptionSet(errors);
        }
        return null;
    }


    public ResponseEntity<?> getProductStockById(Integer id) {
        Object productStock = productRepository.fetchProductStockAndCostById(id);
        if(productStock == null){
            return customExceptionHandler.notFoundException("Product with id " + id + " not found");
        }
        return ResponseEntity.ok(productStock);
    }
}
