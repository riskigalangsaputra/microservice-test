package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.dto.EmailDto;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RedisListenerService implements MessageListener {

    private final EmailService emailService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String msg = message.toString();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            EmailDto emailDto = objectMapper.readValue(msg, EmailDto.class);
            emailService.sendingEmail(emailDto.getTo(), emailDto.getSubject(), emailDto.getBody());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
