package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.dto.UserDto;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> register(@RequestBody UserDto request) {
        return ResponseEntity.ok(userService.save(request));
    }

    @GetMapping("/secured")
    public ResponseEntity<String> securedEndpoint() {
        return ResponseEntity.ok("Helo, from secure endpoint.");
    }
}
