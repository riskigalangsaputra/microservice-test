package org.example.service;

import org.example.dto.LoginDto;
import org.example.dto.RegisterDto;
import org.example.dto.UserDto;

public interface UserService {

    UserDto register(RegisterDto request);
    UserDto login(LoginDto request);
}
