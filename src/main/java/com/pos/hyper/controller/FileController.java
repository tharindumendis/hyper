package com.pos.hyper.controller;

import com.pos.hyper.DTO.CategoryDto;
import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.model.category.CategoryService;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("api/image")
public class FileController {

    @GetMapping("{id}")
    public ResponseEntity<Resource> getImage(@PathVariable Long id) {
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




}
