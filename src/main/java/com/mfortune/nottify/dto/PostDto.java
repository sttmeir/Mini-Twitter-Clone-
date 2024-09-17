package com.mfortune.nottify.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PostDto {
    private String title;
    private String body;
}
