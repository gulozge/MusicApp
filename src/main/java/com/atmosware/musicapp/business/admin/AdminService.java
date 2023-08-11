package com.atmosware.musicapp.business.admin;

import java.util.List;
import java.util.UUID;

public interface AdminService {
    List<AdminResponse> getAll();

    AdminResponse getById(UUID id);

    AdminResponse add(AdminRequest request);

    AdminResponse update(UUID id, AdminRequest request);

    void delete(UUID id);

    LoginResponse login(AdminRequest request);

}
