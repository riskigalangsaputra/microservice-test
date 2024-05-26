package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.*;
import org.example.exception.BadRequestException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RestTemplate restTemplate;
    private final JwtUtils jwtUtils;
    private final IntegrationService integrationService;

    public ResponseDto login(LoginDto request) {
        Map<String, Object> loginResponse = integrationService.callUserService("http://user-service/users/login", request, HttpMethod.POST);

        assert loginResponse != null;
        final String userId = loginResponse.get("id").toString();
        final String email = loginResponse.get("email").toString();

        String accessToken = jwtUtils.generateToken(userId, email, "ACCESS");
        String refreshToken = jwtUtils.generateToken(userId, email, "REFRESH");
        Map<String, Object> data = new HashMap<>();
        data.put("access_token", accessToken);
        data.put("refresh_token", refreshToken);

        return new ResponseDto("00", "Success", data);
    }

    public ResponseDto register(RegisterDto request) {
        if (request == null) {
            throw new BadRequestException("Bad request");
        }

        Map<String, Object> registerResponse = integrationService.callUserService("http://user-service/users/register", request, HttpMethod.POST);
        assert registerResponse != null;
        return new ResponseDto("00", "Success", null);
    }
}
