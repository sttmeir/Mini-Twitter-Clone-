package com.mfortune.nottify.service;

import com.mfortune.nottify.configuration.JwtHelper;
import com.mfortune.nottify.domain.User;
import com.mfortune.nottify.dto.JwtRequest;
import com.mfortune.nottify.dto.RegistrationUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import static com.mfortune.nottify.domain.Role.ROLE_USER;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtHelper jwtHelper;
    private final UserValidationService userValidationService;
    private final PasswordEncoder passwordEncoder;

    public String login(JwtRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(), authRequest.getPassword()
                )
        );
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        return jwtHelper.generateToken(userDetails);
    }

    public User signup(RegistrationUserDto registrationUserDto) {
        userValidationService.validateUser(registrationUserDto);
        User user = new User();
        user.setName(registrationUserDto.getName());
        user.setUsername(registrationUserDto.getUsername());
        user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        user.setEmail(registrationUserDto.getEmail());
        user.setPhoneNumber(registrationUserDto.getPhoneNumber());
        user.setRole(ROLE_USER.name());
        user.setAuthorities(ROLE_USER.getAuthorities());
        return userService.saveUser(user);
    }
}
