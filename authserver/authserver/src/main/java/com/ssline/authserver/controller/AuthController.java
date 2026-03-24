package com.ssline.authserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssline.authserver.dto.TokenDTO;
import com.ssline.authserver.dto.UserDTO;
import com.ssline.authserver.service.IAuthService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    
    private final IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(authService.login(userDTO));
    }

    @PostMapping("/validate")
    public ResponseEntity<TokenDTO> validate(@RequestHeader String accessToken) {
        return ResponseEntity.ok(authService.validateToken(TokenDTO.builder().accessToken(accessToken).build() ));
    }

}
