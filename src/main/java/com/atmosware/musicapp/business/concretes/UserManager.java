package com.atmosware.musicapp.business.concretes;


import com.atmosware.musicapp.business.abstracts.UserService;
import com.atmosware.musicapp.business.dto.requests.UserLoginRequest;
import com.atmosware.musicapp.business.dto.requests.UserRequest;
import com.atmosware.musicapp.business.dto.responses.UserResponse;
import com.atmosware.musicapp.business.rules.UserBusinessRules;
import com.atmosware.musicapp.entities.User;
import com.atmosware.musicapp.entities.enums.Role;
import com.atmosware.musicapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserManager implements UserService {
    private final UserRepository repository;
    private final UserBusinessRules rules;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponse> getAll() {
        return repository.findAll().stream().map(user -> UserResponse.builder().id(user.getId()).firstname(user.getFirstname()).lastname(user.getLastname()).userName(user.getUserName()).email(user.getEmail()).build()).toList();
    }

    @Override
    public UserResponse getById(UUID id) {
        User user = repository.findById(id).orElseThrow();
        return UserResponse.builder().id(user.getId()).firstname(user.getFirstname()).lastname(user.getLastname()).userName(user.getUserName()).email(user.getEmail()).build();
    }

    @Override
    public UserResponse add(UserRequest request) {
        rules.checkIfUserNameExists(request.getUserName());
        User user = new User();
        user.setId(null);
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        repository.save(user);
        return UserResponse.builder().id(user.getId()).firstname(user.getFirstname()).lastname(user.getLastname()).userName(user.getUserName()).email(user.getEmail()).build();
    }

    @Override
    public UserResponse update(UUID id, UserRequest request) {
        rules.checkIfUserExists(id);
        rules.checkIfUserNameExists(request.getUserName());
        User user = new User();
        user.setId(id);
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        repository.save(user);
        return UserResponse.builder().id(user.getId()).firstname(user.getFirstname()).lastname(user.getLastname()).userName(user.getUserName()).email(user.getEmail()).build();
    }

    @Override
    public void delete(UUID id) {
        rules.checkIfUserExists(id);
        repository.deleteById(id);
    }

    public void login(UserLoginRequest request) {
        rules.checkIfUserNameNotExists(request.getUserName());
        rules.passwordCorrect(request);
    }

}