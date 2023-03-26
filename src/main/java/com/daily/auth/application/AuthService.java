package com.daily.auth.application;

import com.daily.user.domain.User;
import com.daily.user.dto.UserJoinRequest;
import com.daily.user.dto.UserLoginRequest;
import com.daily.user.dto.UserLoginResponse;
import com.daily.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;

    public UserLoginResponse join(UserJoinRequest joinRequest) {
        User user = userRepository.save(joinRequest.toUser());

        return new UserLoginResponse(user);
    }

    @Transactional(readOnly = true)
    public UserLoginResponse login(UserLoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getUsername());

        if(user == null) {
            throw new UsernameNotFoundException("User Not Found");
        }

        return new UserLoginResponse(user);
    }

}