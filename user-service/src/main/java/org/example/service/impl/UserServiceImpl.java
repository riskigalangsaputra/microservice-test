package org.example.service.impl;

import lombok.AllArgsConstructor;
import org.example.dto.LoginDto;
import org.example.dto.RegisterDto;
import org.example.dto.UserDto;
import org.example.entity.Users;
import org.example.exception.BadRequestException;
import org.example.exception.ConflictException;
import org.example.exception.UnAuthorizedException;
import org.example.repository.UsersRepository;
import org.example.service.UserService;
import org.example.utils.PasswordUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UsersRepository usersRepository;

    public UserDto login(LoginDto request) {
        Users user = usersRepository.getFirstByEmail(request.getEmail()).orElseThrow(() -> new UnAuthorizedException("Username or password not match"));
        if (!user.getPassword().equals(PasswordUtils.encodeBase64(request.getPassword()))) {
            throw new UnAuthorizedException("Username or password not match");
        }
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .active(user.getActive())
                .build();
    }

    public UserDto register(RegisterDto request) {
        if (request == null) {
            throw new BadRequestException("Request is required");
        }

        var checkUserEmail = usersRepository.existsByEmail(request.getEmail());
        if (checkUserEmail) {
            throw new ConflictException("Email is already registered");
        }

        Users users = new Users();
        users.setName(request.getName());
        users.setEmail(request.getEmail());
        users.setActive(false);
        users.setPassword(PasswordUtils.encodeBase64(request.getPassword()));
        usersRepository.save(users);
        return UserDto.builder()
                .email(users.getEmail())
                .name(users.getName())
                .active(users.getActive())
                .build();
    }
}
