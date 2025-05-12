package com.pos.hyper.model;

import com.pos.hyper.repository.OrgRepository;
import com.pos.hyper.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OrgService {

    private final OrgRepository orgRepository;
    private final OrgMapper orgMapper;
    private final UserRepository userRepository;

    public OrgService(OrgRepository orgRepository, OrgMapper orgMapper, UserRepository userRepository) {
        this.orgRepository = orgRepository;
        this.orgMapper = orgMapper;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> getOrg(){
        return ResponseEntity.ok(orgRepository.findAll().getFirst());
    }
    public ResponseEntity<?> createOrg(OrgDto org){
        Organization existingOrg = orgRepository.findAll().getFirst();
        if(existingOrg != null){
            return ResponseEntity.badRequest().body("Organization already exists");
        }
        return ResponseEntity.ok(orgRepository.save(orgMapper.toEntity(org)));
    }
    public ResponseEntity<?> updateOrg(OrgDto org){
        Organization existingOrg = orgRepository.findAll().getFirst();
        if(org.isActive() != null) {
            existingOrg.setIsActive(org.isActive());
        }
        if(org.address() != null) {
            existingOrg.setAddress(org.address());
        }
        if(org.employeeCount() > userRepository.count()) {
            existingOrg.setEmployeeCount(org.employeeCount());
        }
        if(org.email() != null) {
            existingOrg.setEmail(org.email());
        }
        if(org.logo() != null) {
            existingOrg.setLogo(org.logo());
        }
        if(org.name() != null) {
            existingOrg.setName(org.name());
        }
        if(org.phone() != null) {
            existingOrg.setPhone(org.phone());
        }
        if(org.website() != null) {
            existingOrg.setWebsite(org.website());
        }
        return ResponseEntity.ok(orgRepository.save(existingOrg));
    }
}
