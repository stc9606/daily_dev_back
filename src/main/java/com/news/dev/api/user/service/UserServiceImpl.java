package com.news.dev.api.user.service;

import com.news.dev.api.user.dto.UserDto;
import com.news.dev.api.user.dto.UserJoinRequest;
import com.news.dev.api.user.dto.UserLoginResponse;
import com.news.dev.api.user.entity.UserEntity;
import com.news.dev.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    public UserLoginResponse join(UserJoinRequest joinRq) throws Exception {

        UserEntity entity = new ModelMapper().map(joinRq,  UserEntity.class);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));

        userRepository.save(entity);

        UserLoginResponse loginRs = new ModelMapper().map(entity, UserLoginResponse.class);

        return loginRs;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public UserDto getUserByUserNo(String userNo) throws Exception {
        UserEntity userEntity = userRepository.findByUserNo(userNo);

        if(userEntity == null) {
            throw new UsernameNotFoundException("User Not Found");
        }

        UserDto resultUser = new ModelMapper().map(userEntity, UserDto.class);
        return resultUser;
    }

    @Override
    public UserDto getUserByUserEmail(String email) throws Exception {
        UserEntity userEntity = userRepository.findByEmail(email);

        if(userEntity == null) {
            throw new UsernameNotFoundException("User Not Found");
        }

        UserDto resultUser = new ModelMapper().map(userEntity, UserDto.class);
        return resultUser;
    }
}
