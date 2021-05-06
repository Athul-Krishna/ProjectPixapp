package com.athul.pixapp.api.users.services;

import com.athul.pixapp.api.users.shared.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsersService extends UserDetailsService {
    UserDto createUser(UserDto userDto);
    UserDto getUserDetailsByEmail(String email);
    UserDto getUserByUserId(String userId);
}
