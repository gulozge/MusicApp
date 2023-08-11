package com.atmosware.musicapp.entity;

import com.atmosware.musicapp.entity.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

}