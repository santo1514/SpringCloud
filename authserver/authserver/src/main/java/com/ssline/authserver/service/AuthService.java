package com.ssline.authserver.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ssline.authserver.dto.TokenDTO;
import com.ssline.authserver.dto.UserDTO;
import com.ssline.authserver.entity.UserEntity;
import com.ssline.authserver.helper.JwtHelper;
import com.ssline.authserver.repository.IUserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;


@Transactional
@Service
@AllArgsConstructor
public class AuthService implements IAuthService{

    private IUserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;
    private final static String  EXCEPTION_MESSAGE = "User not authorize.";

    @Override
    public TokenDTO login(UserDTO userDTO) {
        final var userFromDB = userRepository.findByUsername(userDTO.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, EXCEPTION_MESSAGE));
        validPassword(userDTO, userFromDB);
        return TokenDTO.builder()
                .accessToken(jwtHelper.createToken(userFromDB.getUsername()))
                .build();
    }

    @Override
    public TokenDTO validateToken(TokenDTO tokenDTO) {
        if(jwtHelper.validateToken(tokenDTO.getAccessToken())){
            return TokenDTO.builder()
                    .accessToken(tokenDTO.getAccessToken())
                    .build();
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, EXCEPTION_MESSAGE);
    }

    private void validPassword(UserDTO userDTO, UserEntity userEntity){
        if(!passwordEncoder.matches(userDTO.getPassword(), userEntity.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, EXCEPTION_MESSAGE);
        }
    }
    
    
}
