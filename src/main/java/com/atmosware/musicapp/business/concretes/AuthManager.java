package com.atmosware.musicapp.business.concretes;

import com.atmosware.musicapp.business.abstracts.AuthService;
import com.atmosware.musicapp.business.dto.requests.LoginRequest;
import com.atmosware.musicapp.business.dto.responses.JwtAuthenticationResponse;
import com.atmosware.musicapp.common.configuration.security.JwtTokenProvider;
import com.atmosware.musicapp.entities.BaseUser;
import com.atmosware.musicapp.repository.AdminRepository;
import com.atmosware.musicapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthManager implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        BaseUser userDetails = userRepository.findByUserName(loginRequest.getUsernameOrEmail());
        if (userDetails == null) {
            userDetails = adminRepository.findByEmail(loginRequest.getUsernameOrEmail());
        }

        if (userDetails == null) {
            throw new UsernameNotFoundException("User not found with username or email : " + loginRequest.getUsernameOrEmail());
        }

        return JwtAuthenticationResponse.builder()
                .accessToken(jwt)
                .tokenType("Bearer")
                .userId(userDetails.getId())
                .username(userDetails.getEmail())
                .build();
    }
}
