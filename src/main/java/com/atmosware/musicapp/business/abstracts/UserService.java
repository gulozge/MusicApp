package com.atmosware.musicapp.business.abstracts;


import com.atmosware.musicapp.business.dto.requests.UserLoginRequest;
import com.atmosware.musicapp.business.dto.requests.UserRequest;
import com.atmosware.musicapp.business.dto.responses.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserResponse> getAll();

    UserResponse getById(UUID id);

    UserResponse add(UserRequest request);

    UserResponse update(UUID id, UserRequest request);

    void delete(UUID id);

    void login(UserLoginRequest request);
}