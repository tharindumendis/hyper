package com.pos.hyper.service;

import com.pos.hyper.DTO.GRNDto;
import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.model.grn.GRN;
import com.pos.hyper.model.grn.GRNMapper;
import com.pos.hyper.repository.GRNRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class GRNService {
    private final GRNMapper grnMapper;
    private final GRNRepository grnRepository;
    private final CustomExceptionHandler customExceptionHandler;

    public GRNService(GRNMapper grnMapper, GRNRepository grnRepository, CustomExceptionHandler customExceptionHandler) {
        this.grnMapper = grnMapper;
        this.grnRepository = grnRepository;
        this.customExceptionHandler = customExceptionHandler;
    }


    public ResponseEntity<?> createGRN(GRNDto grnDto) {
        GRN grn = grnMapper.toGRN(grnDto);
        grn.setId(null);
        grn.setTotal(0.0);
        grn = grnRepository.save(grn);
        return ResponseEntity.ok(grnMapper.toGRNDto(grn));
    }
    @Transactional
    public ResponseEntity<?> updateGRN(Integer id, GRNDto grnDto) {
        GRN grn = grnRepository.findById(id).orElse(null);
        if(grn == null) {
            return customExceptionHandler.notFoundException("GRN with id " + id + " not found");
        }
        double total = grn.getTotal();
        grn = grnMapper.toGRN(grnDto, grn);
        grn.setTotal(total);
        grn = grnRepository.save(grn);
        return ResponseEntity.ok(grnMapper.toGRNDto(grn));
    }
    public ResponseEntity<?> getAllGRNs() {
        List<GRN> GRNs = grnRepository.findAll();
        return ResponseEntity.ok(GRNs.stream().map(grnMapper::toGRNDto).toList());

    }
    public ResponseEntity<?> getGRNById(Integer id) {
        GRN grn = grnRepository.findById(id).orElse(null);
        if (grn == null) {
            return customExceptionHandler.notFoundException("GRN with id " + id + " not found");
        }
        return ResponseEntity.ok(grnMapper.toGRNDto(grn));
    }
    public ResponseEntity<?> deleteGRN(Integer id) {
        GRN grn = grnRepository.findById(id).orElse(null);
        if(grn == null) {
            return customExceptionHandler.notFoundException("GRN with id " + id + " not found");
        }
        grnRepository.delete(grn);
        return ResponseEntity.ok().build();

    }


}