package com.atmosware.musicapp.business.concretes;


import com.atmosware.musicapp.business.abstracts.UserService;
import com.atmosware.musicapp.business.dto.requests.UserLoginRequest;
import com.atmosware.musicapp.business.dto.requests.UserRequest;
import com.atmosware.musicapp.business.dto.responses.UserResponse;
import com.atmosware.musicapp.business.rules.FollowerBusinessRules;
import com.atmosware.musicapp.business.rules.UserBusinessRules;
import com.atmosware.musicapp.entities.User;
import com.atmosware.musicapp.entities.enums.Role;
import com.atmosware.musicapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserManager implements UserService {
    private final UserRepository repository;
    private final UserBusinessRules rules;
    private final FollowerBusinessRules followerBusinessRules;
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

    @Override
    @Transactional
    public void follow(UUID userId, UUID followedId) {
        rules.checkIfUserExists(userId);
        rules.checkIfUserExists(followedId);
        followerBusinessRules.checkIfUserAndFollowedSomePerson(userId, followedId);
        User user = repository.findById(userId).orElseThrow();
        User followUser = repository.findById(followedId).orElseThrow();
        followerBusinessRules.checkIfAlreadyFollow(user, followUser);
        user.getFollowing().add(followUser);
        repository.save(user);

    }

    @Override
    @Transactional
    public void unfollow(UUID userId, UUID followedId) {
        rules.checkIfUserExists(userId);
        rules.checkIfUserExists(followedId);
        User user = repository.findById(userId).orElseThrow();
        User unfollowUser = repository.findById(followedId).orElseThrow();
        user.getFollowing().remove(unfollowUser);
        repository.save(user);
    }

    @Override
    public List<UserResponse> getFollowing(UUID userId) {
        rules.checkIfUserExists(userId);
        User user = repository.findById(userId).orElseThrow();
        return user.getFollowing()
                .stream()
                .map(followedUser -> UserResponse
                        .builder()
                        .id(followedUser.getId())
                        .firstname(followedUser.getFirstname())
                        .lastname(followedUser.getLastname())
                        .userName(followedUser.getUserName())
                        .email(followedUser.getEmail())
                        .build())
                .toList();
    }

}