package com.atmosware.musicapp.business.rules;

import com.atmosware.musicapp.business.dto.requests.AdminRequest;
import com.atmosware.musicapp.common.constants.Messages;
import com.atmosware.musicapp.core.exceptions.BusinessException;
import com.atmosware.musicapp.entities.Admin;
import com.atmosware.musicapp.repository.AdminRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AdminBusinessRules {
    private AdminRepository repository;
    private PasswordEncoder passwordEncoder;

    public void checkIfAdminExists(UUID id) {
        if (!repository.existsById(id)) {
            throw new BusinessException(Messages.Admin.NotFound);
        }
    }

    public void checkIfEmailExists(String email) {
        if (repository.existsByEmail(email)) {
            throw new BusinessException(Messages.Admin.Exists);
        }
    }

    public void checkIfEmailNotExists(String email) {
        if (!repository.existsByEmail(email)) {
            throw new BusinessException(Messages.Admin.NotCorrect);
        }
    }

    public void passwordCorrect(AdminRequest request) {
        Admin admin = repository.findByEmail(request.getEmail());
        boolean password = passwordEncoder.matches(request.getPassword(), admin.getPassword());
        if (!password) {
            throw new BusinessException(Messages.Admin.NotCorrect);
        }
    }
}
