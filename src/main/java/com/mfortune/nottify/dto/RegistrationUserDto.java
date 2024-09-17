package com.mfortune.nottify.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data // @ToString, @EqualsAndHashCode, @Getter / @Setter and @RequiredArgsConstructor
public class RegistrationUserDto {

    @NotBlank(message = "Name must not be empty!")
    @Size(min = 2, max = 30, message = "Your name must be between 2 and 30 characters!")
    private String name;

    @NotBlank(message = "Username must not be empty!")
    @Size(min = 2, max = 20, message = "Your username must be between 2 and 20 characters!")
    private String username;

    @NotBlank(message = "Password must not be empty!")
    @Size(min = 5, max = 15, message = "Your password must be between 5 and 15 characters!")
    private String password;

    private String confirmPassword;

    @NotBlank(message = "Email must not be empty!")
    @Email(message = "Email must be valid!")
    private String email;

    @NotBlank(message = "Phone number must not be empty!")
    private String phoneNumber;
}
