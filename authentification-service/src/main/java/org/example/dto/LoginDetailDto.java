package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDetailDto {

    private String email;
    private String name;
    private Boolean isActive;
    private String accessToken;
    private String refreshToken;
}
