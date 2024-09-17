package com.mfortune.nottify.service;

import com.mfortune.nottify.domain.User;
import com.mfortune.nottify.dto.RegistrationUserDto;
import com.mfortune.nottify.exceptions.DuplicateUsernameException;
import com.mfortune.nottify.exceptions.PasswordsDoNotMatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class UserValidationService {

    private final UserService userService;

    public void validateUser(RegistrationUserDto registrationUserDto) {
        if(!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            throw new PasswordsDoNotMatchException("Passwords do not match!");
        }
        if(userService.getUserByUsername(registrationUserDto.getUsername()).isPresent()) {
            throw new DuplicateUsernameException("Username is already in use!");
        }
        if(userService.getUserByEmail(registrationUserDto.getEmail()).isPresent()) {
            throw new DuplicateUsernameException("Email is already in use!");
        }
        if(userService.getUserByPhoneNumber(registrationUserDto.getPhoneNumber()).isPresent()) {
            throw new DuplicateUsernameException("Phone number is already in use!");
        }
    }
}
