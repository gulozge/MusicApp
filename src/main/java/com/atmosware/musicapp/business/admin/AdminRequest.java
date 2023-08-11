package com.atmosware.musicapp.business.admin;


import com.atmosware.musicapp.constants.Regex;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AdminRequest {
    @Pattern(regexp = Regex.email)
    private String email;
    @Pattern(regexp = Regex.password)
    private String password;

}
