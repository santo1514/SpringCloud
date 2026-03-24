package com.ssline.authserver.service;

import com.ssline.authserver.dto.TokenDTO;
import com.ssline.authserver.dto.UserDTO;

public interface IAuthService {
    
    TokenDTO login(UserDTO userDTO);
    TokenDTO validateToken(TokenDTO tokenDTO);
}
