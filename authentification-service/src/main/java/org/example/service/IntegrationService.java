package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.exception.BadRequestException;
import org.example.exception.ConflictException;
import org.example.exception.UnAuthorizedException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class IntegrationService {

    private final RestTemplate restTemplate;

    public Map<String, Object> callUserService(String url, Object request, HttpMethod httpMethod) {
        try {
            if (HttpMethod.POST.equals(httpMethod)) {
                Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);
                return response;
            } else {
                Map<String, Object> response = restTemplate.getForObject(url, Map.class);
                return response;
            }
        } catch (HttpClientErrorException e) {
            HttpStatusCode statusCode = e.getStatusCode();
            log.error("Client error: {}", statusCode);
            switch (statusCode) {
                case HttpStatus.UNAUTHORIZED -> {
                    log.error("Unauthorized: {}", e.getMessage());
                    throw new UnAuthorizedException("Unauthorized"); // message is manually
                }
                case HttpStatus.BAD_REQUEST -> {
                    log.error("Bad Request: {}", e.getResponseBodyAsString());
                    throw new BadRequestException("Bad Request");
                }
                case HttpStatus.CONFLICT -> {
                    log.error("Conflict: {}", e.getResponseBodyAsString());
                    throw new ConflictException("Conflict");
                }
                default -> {
                    log.error("Client error: {}", e.getResponseBodyAsString());
                    throw e;
                }
            }
        } catch (HttpServerErrorException e) {
            HttpStatusCode statusCode = e.getStatusCode();
            log.error("Server error: {}", statusCode);
            throw e;
        } catch (Exception e) {
            log.error("An error occurred: {}", e.getMessage());
            throw e;
        }
    }
}

