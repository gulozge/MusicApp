package com.atmosware.musicapp.common.configuration.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


import java.util.UUID;

@Service("userSecurityService")
public class UserSecurityService {

    public boolean canAccessUser(Authentication authentication, UUID userId) {
        if (!(authentication.getPrincipal() instanceof UserPrincipal)) {
            return false;
        }

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userPrincipal.getId().equals(userId);
    }
}
