package com.daily.auth.application;

import com.daily.auth.dto.AccessTokenRenewRequest;
import com.daily.auth.dto.AccessTokenRenewResponse;
import com.daily.domain.user.domain.User;
import com.daily.domain.user.dto.UserRequest;
import com.daily.domain.user.repository.UserRepository;
import com.daily.domain.userSites.domain.UserSites;
import com.daily.domain.userSites.repository.UserSitesRepository;
import com.daily.global.common.file.FileUtils;
import com.daily.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService implements UserDetailsService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserSitesRepository userSitesRepository;
    private final FileUtils fileUtils;

    public CommonResponse isCheck(final String email) {
        User user = userRepository.findById(email).orElse(null);

        if (user != null) {
            new DuplicateKeyException("이미 사용 중인 이메일입니다.");
        }

        return new CommonResponse(HttpStatus.OK, "성공" );
    }

    public CommonResponse join(final UserRequest joinRequest) throws IOException {
        User isUser = userRepository.findById(joinRequest.getEmail()).orElse(null);

        if (isUser != null)
            throw new DuplicateKeyException("이미 사용 중인 이메일입니다.");

        String filePath = fileUtils.storeFile(joinRequest.getImageFile());
        userRepository.save(joinRequest.toUser(filePath));

        if (joinRequest.getSiteCodes().size() > 0) {
            List<UserSites> userSites = joinRequest.getSiteCodes().stream()
                    .map(site -> UserSites.builder()
                            .email(joinRequest.getEmail())
                            .siteCode(site)
                            .build())
                    .collect(Collectors.toList());

            userSitesRepository.saveAll(userSites);
        }

        return new CommonResponse(HttpStatus.OK, "성공" );
    }

    public AccessTokenRenewResponse generateAccessToken(final AccessTokenRenewRequest request) {
        String refreshToken = request.getRefreshToken();
        String payload = jwtTokenProvider.getPayload(refreshToken);

        AccessTokenRenewResponse tokenResponse = new AccessTokenRenewResponse(jwtTokenProvider.createAccessToken(payload), jwtTokenProvider.createRefreshToken(payload));
        return tokenResponse;
    }

    @Override
    @SneakyThrows
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException("User is not found"));
        return user;
    }
}
