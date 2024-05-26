package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;
    private String email;
    private String password;
    private String name;
    private boolean active;
}
