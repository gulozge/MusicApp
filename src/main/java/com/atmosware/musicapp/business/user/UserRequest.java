package com.atmosware.musicapp.business.user;


import com.atmosware.musicapp.constants.Regex;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter

public class UserRequest {
    @Length(min = 3, max = 50)
    private String firstname;
    @Length(min = 2, max = 50)
    private String lastname;
    @Length(min = 5, max = 50)
    private String userName;
    @Pattern(regexp = Regex.email)
    private String email;
    @Pattern(regexp = Regex.password)
    private String password;
}
