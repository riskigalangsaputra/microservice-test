package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.AuthRequest;
import org.example.dto.AuthResponse;
import org.example.dto.UserDto;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RestTemplate restTemplate;
    private final JwtUtils jwtUtils;

    public AuthResponse register(AuthRequest request) {
        request.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        UserDto registeredUser = restTemplate.postForObject("http://user-service/users", request, UserDto.class);

        assert registeredUser != null;
        String accessToken = jwtUtils.generateToken(registeredUser.getId(), registeredUser.getRole(), "ACCESS");
        String refreshToken = jwtUtils.generateToken(registeredUser.getId(), registeredUser.getRole(), "REFRESH");

        return new AuthResponse(accessToken, refreshToken);
    }

}
