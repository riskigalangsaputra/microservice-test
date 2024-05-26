package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.*;
import org.example.exception.BadRequestException;
import org.example.exception.UnAuthorizedException;
import org.example.utils.ConverterUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtils jwtUtils;
    private final IntegrationService integrationService;

    public ResponseDto login(LoginDto request) {
        Map<String, Object> loginResponse = integrationService.callUserService("http://user-service/users/login", request, HttpMethod.POST);
        assert loginResponse != null;
        final String userId = loginResponse.get("id").toString();
        final String email = loginResponse.get("email").toString();
        final String name = loginResponse.get("name").toString();
        final boolean isActive = ConverterUtils.convertToBoolean(loginResponse.get("active"));

        if (!isActive) {
            throw new UnAuthorizedException("User is not active");
        }

        final String accessToken = jwtUtils.generateToken(userId, email, "ACCESS");
        final String refreshToken = jwtUtils.generateToken(userId, email, "REFRESH");
        LoginDetailDto data = new LoginDetailDto();
        data.setEmail(email);
        data.setName(name);
        data.setIsActive(true);
        data.setAccessToken(accessToken);
        data.setRefreshToken(refreshToken);
        return new ResponseDto("00", "Success", data);
    }

    public ResponseDto register(RegisterDto request) {
        if (request == null) {
            throw new BadRequestException("Bad request");
        }

        Map<String, Object> registerResponse = integrationService.callUserService("http://user-service/users/register", request, HttpMethod.POST);
        assert registerResponse != null;
        RegisterDetailDto data = new RegisterDetailDto();
        data.setEmail(request.getEmail());
        data.setName(request.getName());
        data.setIsActive(ConverterUtils.convertToBoolean(registerResponse.get("active")));
        return new ResponseDto("00", "Success", data);
    }
}
