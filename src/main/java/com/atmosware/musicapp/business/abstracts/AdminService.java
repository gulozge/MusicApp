package com.atmosware.musicapp.business.abstracts;

import com.atmosware.musicapp.business.dto.requests.AdminRequest;
import com.atmosware.musicapp.business.dto.responses.AdminResponse;

import java.util.List;
import java.util.UUID;

public interface AdminService {
    List<AdminResponse> getAll();

    AdminResponse getById(UUID id);

    AdminResponse add(AdminRequest request);

    AdminResponse update(UUID id, AdminRequest request);

    void delete(UUID id);

    void login(AdminRequest request);

}
