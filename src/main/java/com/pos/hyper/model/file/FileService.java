package com.pos.hyper.model.file;

import com.pos.hyper.DTO.ProductDto;
import com.pos.hyper.model.product.Product;
import com.pos.hyper.model.product.ProductMapper;
import com.pos.hyper.service.ProductService;
import com.pos.hyper.repository.ProductRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;


    public FileService(ProductService productService, ProductMapper productMapper, ProductRepository productRepository) {
        this.productService = productService;
        this.productMapper = productMapper;
        this.productRepository = productRepository;
    }

    public ResponseEntity<Resource> getProductImage(Integer id) {
        String fileName = id + ".jpg"; // Match stored image name
        Path path = Paths.get("uploads/" + fileName);
        try {
            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists()) {
                System.err.println("File not found: " + path.toAbsolutePath());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        } catch (MalformedURLException e) {
            System.err.println("Error serving image: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    public ResponseEntity<Resource> uploadProductImage(Integer id, MultipartFile image) {

        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        System.out.println("request received for image upload. Product ID: " + product.getId());
        productService.uploadImage(image, product);
        System.out.println("Image uploaded successfully");
        return ResponseEntity.created(null).build();
    }

    //this is for all image upload has TO BE DONE (this is copy of uploadImage in product service)
    public ProductDto uploadImageLogo(MultipartFile file, Product product) {
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
        return productMapper.toProductDto(product);
    }
}
