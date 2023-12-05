package com.abcairline.abc.controller;

import com.abcairline.abc.controller.constant.ProviderType;
import com.abcairline.abc.domain.JWToken;
import com.abcairline.abc.domain.User;
import com.abcairline.abc.dto.auth.OauthUserInfo;
import com.abcairline.abc.dto.user.SimpleUserDto;
import com.abcairline.abc.service.UserService;
import com.abcairline.abc.service.auth.JwtService;
import com.abcairline.abc.service.auth.OauthService;
import com.abcairline.abc.service.auth.constant.JwtConstants;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@Slf4j
@RequestMapping("/api/v1/login")
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;
    private final OauthService oauthService;
    private final JwtService jwtService;

    @GetMapping("/token")
    public SimpleUserDto getToken(@RequestParam(name = "code") String code, @RequestParam(name = "provider") String provider
                                    , HttpServletResponse response) throws IOException {
        String accessToken = "";
        OauthUserInfo userInfo = null;

        if (provider.equals("google")) {
            accessToken = oauthService.getAccessToken(code, ProviderType.GOOGLE);
            userInfo = oauthService.getUserInformation(accessToken, ProviderType.GOOGLE);
        } else if (provider.equals("naver")) {
            accessToken = oauthService.getAccessToken(code, ProviderType.NAVER);
            userInfo = oauthService.getUserInformation(accessToken, ProviderType.NAVER);

        } else if (provider.equals("kakao")) {
            accessToken = oauthService.getAccessToken(code, ProviderType.KAKAO);
            userInfo = oauthService.getUserInformation(accessToken, ProviderType.KAKAO);
        }

        User user = null;

        if (userInfo != null) {
            user = userService.retrieveUserWithProviderAndProviderId(userInfo.getProvider(), userInfo.getProviderId());

            if (user == null) {
                // 회원가입
                user = User.builder().email(userInfo.getEmail())
                        .name(userInfo.getName())
                        .imageUrl(userInfo.getImageUrl())
                        .socialLoginYn(true)
                        .provider(userInfo.getProvider())
                        .providerId(userInfo.getProviderId())
                        .signUpDate(LocalDateTime.now())
                        .lastAccessDate(LocalDateTime.now())
                        .role("ROLE_USER")
                        .build();
            }else {
                // 로그인 (최신 사용자 정보로 업데이트)
                user.setName(userInfo.getName());
                user.setImageUrl(userInfo.getImageUrl());
                user.setLastAccessDate(LocalDateTime.now());
            }
            userService.saveUser(user);
        }

        JWToken token = jwtService.createToken(user);
        response.addHeader(JwtConstants.HEADER_AUTHORIZATION, JwtConstants.TOKEN_PREFIX + token.getAccessToken());

        return new SimpleUserDto(user);
    }
}
