package com.atmosware.musicapp.business.concretes;

import com.atmosware.musicapp.business.abstracts.AdminService;
import com.atmosware.musicapp.business.dto.requests.AdminRequest;
import com.atmosware.musicapp.business.dto.responses.AdminResponse;
import com.atmosware.musicapp.business.rules.AdminBusinessRules;
import com.atmosware.musicapp.entities.Admin;
import com.atmosware.musicapp.entities.enums.Role;
import com.atmosware.musicapp.repository.AdminRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@AllArgsConstructor
public class AdminManager implements AdminService {
    private final AdminRepository repository;
    private final AdminBusinessRules rules;
    private final PasswordEncoder passwordEncoder;


    @Override
    public List<AdminResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(admin -> AdminResponse
                        .builder()
                        .id(admin.getId())
                        .email(admin.getEmail())
                        .build())
                .toList();
    }

    @Override
    public AdminResponse getById(UUID id) {
        Admin admin = repository.findById(id).orElseThrow();
        return AdminResponse
                .builder()
                .id(admin.getId())
                .email(admin.getEmail())
                .build();
    }

    @Override
    public AdminResponse add(AdminRequest request) {
        rules.checkIfEmailExists(request.getEmail());
        Admin admin = new Admin();
        admin.setId(null);
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setRole(Role.ADMIN);
        repository.save(admin);
        return AdminResponse.builder()
                .id(admin.getId())
                .email(admin.getEmail())
                .build();
    }

    @Override
    public AdminResponse update(UUID id, AdminRequest request) {
        rules.checkIfAdminExists(id);
        rules.checkIfEmailExists(request.getEmail());
        Admin admin = new Admin();
        admin.setId(id);
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setRole(Role.ADMIN);
        repository.save(admin);
        return AdminResponse.builder()
                .id(admin.getId())
                .email(admin.getEmail())
                .build();
    }

    public void login(AdminRequest request) {
        rules.checkIfEmailNotExists(request.getEmail());
        rules.passwordCorrect(request);
    }

    @Override
    public void delete(UUID id) {
        rules.checkIfAdminExists(id);
        repository.deleteById(id);
    }


}
