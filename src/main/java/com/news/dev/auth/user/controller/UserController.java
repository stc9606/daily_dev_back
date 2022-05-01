package com.news.dev.auth.user.controller;

import com.news.dev.auth.user.dto.UserDto;
import com.news.dev.auth.user.dto.UserJoinRequest;
import com.news.dev.auth.user.dto.UserLoginRequest;
import com.news.dev.auth.user.dto.UserLoginResponse;
import com.news.dev.auth.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<UserLoginResponse> join(@RequestBody UserJoinRequest joinRq) throws Exception {
        UserLoginResponse loginRs = null;

        loginRs  = userService.join(joinRq);

        return ResponseEntity.status(HttpStatus.CREATED).body(loginRs);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest loginRq) throws Exception {
        UserLoginResponse loginRs = userService.login(loginRq);

        return ResponseEntity.status(HttpStatus.OK).body(loginRs);
    }

    @GetMapping("/info")
    public ResponseEntity<UserDto> info(@RequestBody UserDto requestDto) throws Exception {
        UserDto responseDto = userService.getUserByUsername(requestDto.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

}
