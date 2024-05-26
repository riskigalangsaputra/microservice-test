package org.example.service;

import lombok.AllArgsConstructor;
import org.example.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class UserService {

    public UserDto save(UserDto request) {
        String userId = String.valueOf(new Date().getTime());
        request.setId(userId);
        request.setRole("USER");
        return request;
    }

}
