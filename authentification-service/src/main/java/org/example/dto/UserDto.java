package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class UserDto {

    private Long id;
    private String email;
    private String password;
    private String name;
}
