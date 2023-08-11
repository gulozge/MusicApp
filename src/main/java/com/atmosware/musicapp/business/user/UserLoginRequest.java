package com.atmosware.musicapp.business.user;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequest {
    @NotBlank
    private String userName;
    @NotBlank
    private String password;
}
