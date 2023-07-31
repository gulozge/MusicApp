package com.atmosware.musicapp.common.configuration.security;

import com.atmosware.musicapp.entities.Admin;
import com.atmosware.musicapp.entities.User;
import com.atmosware.musicapp.repository.AdminRepository;
import com.atmosware.musicapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {


  private final  UserRepository userRepository;

  private final  AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(usernameOrEmail);
        if (user != null) {
            return UserPrincipal.create(user);
        }

        Admin admin = adminRepository.findByEmail(usernameOrEmail);
        if (admin != null) {
            return UserPrincipal.create(admin);
        }

        throw new UsernameNotFoundException("User not found with username or email : " + usernameOrEmail);
    }

    public UserDetails loadUserById(UUID id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            return UserPrincipal.create(user);
        }

        Admin admin = adminRepository.findById(id).orElse(null);
        if (admin != null) {
            return UserPrincipal.create(admin);
        }

        throw new UsernameNotFoundException("User not found with id : " + id);
    }
}
