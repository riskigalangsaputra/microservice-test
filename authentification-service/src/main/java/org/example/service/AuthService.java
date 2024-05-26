package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.*;
import org.example.exception.BadRequestException;
import org.example.exception.UnAuthorizedException;
import org.example.utils.ConverterUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final JwtUtils jwtUtils;
    private final IntegrationService integrationService;
    private final RedisTemplate redisTemplate;
    private final ChannelTopic channelTopic;

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
        final String email = registerResponse.get("email").toString();
        sendingEmail(email, "Please activate your account, by clicking the following link : " + "http://localhost:8080/auth/activation");

        RegisterDetailDto data = new RegisterDetailDto();
        data.setEmail(request.getEmail());
        data.setName(request.getName());
        data.setIsActive(ConverterUtils.convertToBoolean(registerResponse.get("active")));
        return new ResponseDto("00", "Success", data);
    }

    private void sendingEmail(String to, String body) {
        log.debug("To : {}, Body : {}", to, body);
        EmailDto emailDto = new EmailDto();
        emailDto.setTo(to);
        emailDto.setSubject("Verification");
        emailDto.setBody(body);
        redisTemplate.convertAndSend(channelTopic.getTopic(), emailDto);
    }
}
