package com.atmosware.musicapp.business.admin;

import com.atmosware.musicapp.constants.Messages;
import com.atmosware.musicapp.entity.Admin;
import com.atmosware.musicapp.entity.enums.Role;
import com.atmosware.musicapp.exception.BusinessException;
import com.atmosware.musicapp.repository.AdminRepository;
import com.atmosware.musicapp.business.token.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminManager implements AdminService {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final AdminRepository repository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public List<AdminResponse> getAll() {
        List<AdminResponse> admins = repository.findAll()
                .stream()
                .map(admin -> AdminResponse
                        .builder()
                        .id(admin.getId())
                        .email(admin.getEmail())
                        .build())
                .toList();
        log.info("Found {} admins", admins.size());
        return admins;
    }

    @Override
    public AdminResponse getById(UUID id) {
        Admin admin = repository.findById(id).orElseThrow();
        log.info("Found admin with id: {}", id);
        return AdminResponse
                .builder()
                .id(admin.getId())
                .email(admin.getEmail())
                .build();
    }

    @Override
    public AdminResponse add(AdminRequest request) {
        log.info("Adding new admin with email: {}...", request.getEmail());
        checkIfEmailExists(request.getEmail());
        Admin admin = new Admin();
        admin.setId(null);
        return saveAdminAndReturnResponse(admin, request);
    }

    @Override
    public AdminResponse update(UUID id, AdminRequest request) {
        log.info("Updating admin with id: {}...", id);
        checkIfEmailExists(request.getEmail());
        Admin admin = repository.findById(id).orElseThrow();
        admin.setId(id);

        return saveAdminAndReturnResponse(admin, request);
    }

    public LoginResponse login(AdminRequest request) {
        checkAuth(request.getEmail(), request.getPassword());
        LoginResponse loginResponse = new LoginResponse(generateToken(request.getEmail()));
        log.info("Admin login successfully with email: {}", request.getEmail());
        return loginResponse;
    }

    @Override
    public void delete(UUID id) {
        log.warn("Deleting admin with id: {}...", id);
        checkIfAdminExists(id);
        repository.deleteById(id);
        log.info("Admin deleted successfully with id: {}", id);
    }
    private AdminResponse saveAdminAndReturnResponse(Admin admin, AdminRequest request) {
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setRole(Role.ADMIN);
        repository.save(admin);
        log.info("{} successfully with id: {}", admin.getId() == null ? "Admin added" : "Admin updated", admin.getId());

        return AdminResponse.builder()
                .id(admin.getId())
                .email(admin.getEmail())
                .build();
    }
    private void checkIfAdminExists(UUID id) {
        if (!repository.existsById(id)) {
            log.error("Admin not found with id: {}", id);
            throw new BusinessException(Messages.Admin.NOT_FOUND);
        }
    }

    private void checkIfEmailExists(String email) {
        if (repository.existsByEmail(email)) {
            log.error("Email already exists: {}", email);
            throw new BusinessException(Messages.Admin.EXISTS);
        }
    }

    private void checkAuth(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            log.info("Authentication successful for email: {}", email);
        } catch (BusinessException e) {
            log.error("Authentication failed for email: {}", email, e);
            throw new BusinessException(Messages.Admin.NOT_CORRECT);
        }
    }

    private String generateToken(String email) {
        Admin admin = repository.findByEmail(email);
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("adminId", admin.getId());
        String token = tokenService.generateToken(extraClaims, admin);
        log.info("Token generation completed for email: {}", email);
        return token;
    }
}
