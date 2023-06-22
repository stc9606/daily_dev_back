package com.daily.auth.presentation;

import com.daily.auth.application.AuthService;
import com.daily.auth.dto.AccessTokenRenewRequest;
import com.daily.auth.dto.AccessTokenRenewResponse;
import com.daily.global.common.response.CommonResponse;
import com.daily.domain.user.dto.UserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }

    @GetMapping("/isCheck")
    public CommonResponse isCheck(@NotBlank(message = "이메일은 필수 입력 값 입니다.") @RequestParam String email) {
        return authService.isCheck(email);
    }

    @PostMapping("/token/access")
    public CommonResponse token(@RequestBody @Valid AccessTokenRenewRequest tokenRenewRequest) {
        AccessTokenRenewResponse accessTokenRenewResponse = authService.generateAccessToken(tokenRenewRequest);
        return new CommonResponse(HttpStatus.OK, "성공" );
    }

    @PostMapping("/join")
    public CommonResponse join(@Valid @RequestBody UserRequest joinRequest) throws IOException {
        return authService.join(joinRequest);
    }

}
