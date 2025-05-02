package com.pos.hyper.model.grn;

import com.pos.hyper.DTO.GRNDto;
import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.repository.GRNRepository;
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


    public GRNDto createGRN(GRNDto grnDto) {
        GRN grn = grnMapper.toGRN(grnDto);
        grn.setId(null);
        grn.setTotal(0.0);
        grn = grnRepository.save(grn);
        return grnMapper.toGRNDto(grn);
    }
    @Transactional
    public GRNDto updateGRN(Integer id, GRNDto grnDto) {
        GRN grn = grnRepository.findById(id)
                .orElseThrow(() -> customExceptionHandler.handleNotFoundException("GRN with id " + id + " not found"));
        double total = grn.getTotal();
        grn = grnMapper.toGRN(grnDto, grn);
        grn.setTotal(total);
        grn = grnRepository.save(grn);
        return grnMapper.toGRNDto(grn);
    }
    public List<GRNDto> getAllGRNs() {
        List<GRN> inventories = grnRepository.findAll();
        return inventories.stream().map(grnMapper::toGRNDto).toList();
    }
    public GRNDto getGRNById(Integer id) {
        GRN grn = grnRepository.findById(id)
                .orElseThrow(() -> customExceptionHandler.handleNotFoundException("GRN with id " + id + " not found"));
        return grnMapper.toGRNDto(grn);
    }
    public void deleteGRN(Integer id) {
        GRN grn = grnRepository.findById(id)
                .orElseThrow(() -> customExceptionHandler.handleNotFoundException("GRN with id " + id + " not found"));
        grnRepository.delete(grn);
    }


}
