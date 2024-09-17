package com.mfortune.nottify.resource;

import com.mfortune.nottify.domain.User;
import com.mfortune.nottify.dto.JwtRequest;
import com.mfortune.nottify.dto.JwtResponse;
import com.mfortune.nottify.dto.RegistrationUserDto;
import com.mfortune.nottify.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class  AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@Valid @RequestBody JwtRequest authRequest) {
        String token = authService.login(authRequest);
        return new ResponseEntity<>(new JwtResponse(token), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> createNewUser(@Valid @RequestBody RegistrationUserDto registrationUserDto) {
        User user = authService.signup(registrationUserDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
