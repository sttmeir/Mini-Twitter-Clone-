package com.mfortune.nottify.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class JwtRequest {
    public String username;
    public String password;
}
