package com.pos.hyper.controller;

import com.pos.hyper.model.file.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/image")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }


    @GetMapping("/product/{id}")
    public ResponseEntity<Resource> getProductImage(@PathVariable Integer id) {
        return fileService.getProductImage(id);
    }
    @PostMapping("/product/{id}")
    public ResponseEntity<Resource> uploadProductImage(@PathVariable Integer id, @RequestParam("image") MultipartFile image) {
        return fileService.uploadProductImage(id, image);
    }
    @PutMapping("/product/{id}")
    public ResponseEntity<Resource> replaceProductImage(@PathVariable Integer id, @RequestParam("image") MultipartFile image) {
        return fileService.uploadProductImage(id, image);
    }







}
