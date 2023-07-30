package com.atmosware.musicapp.business.rules;

import com.atmosware.musicapp.business.dto.requests.UserLoginRequest;
import com.atmosware.musicapp.common.constants.Messages;
import com.atmosware.musicapp.core.exceptions.BusinessException;
import com.atmosware.musicapp.entities.User;
import com.atmosware.musicapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserBusinessRules {
    private UserRepository repository;
    private PasswordEncoder passwordEncoder;

    public void checkIfUserExists(UUID id) {
        if (!repository.existsById(id)) {
            throw new BusinessException(Messages.User.NotFound);
        }
    }

    public void checkIfUserNameExists(String email) {
        if (repository.existsByUserName(email)) {
            throw new BusinessException(Messages.User.Exists);
        }
    }

    public void checkIfUserNameNotExists(String email) {
        if (!repository.existsByUserName(email)) {
            throw new BusinessException(Messages.User.NotCorrect);
        }
    }

    public void passwordCorrect(UserLoginRequest request) {
        User user = repository.findByUserName(request.getUserName());
        boolean password = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!password) {
            throw new BusinessException(Messages.User.NotCorrect);
        }
    }
}