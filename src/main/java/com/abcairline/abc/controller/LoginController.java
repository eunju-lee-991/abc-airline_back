package com.abcairline.abc.controller;

import com.abcairline.abc.controller.constant.OAuthConstants;
import com.abcairline.abc.domain.User;
import com.abcairline.abc.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/v1/login")
@RequiredArgsConstructor
public class LoginController {

    @Value("${google.client.id}")
    private String googleClientId;

    @Value("${google.client.secret}")
    private String googleClientSecret;

    @Value("${google.redirect-uri}")
    private String googleRedirectUri;

    @Value("${naver.client.id}")
    private String naverClientId;

    @Value("${naver.client.secret}")
    private String naverClientSecret;

    @Value("${naver.redirect-uri}")
    private String naverRedirectUri;

    @Value("${kakao.client.id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-uri}")
    private String KaKaoRedirectUri;

    private final UserService userService;

    @GetMapping("/token")
    public String getToken(@RequestParam(name = "code") String code) throws IOException {
        log.info("code = " + code);

        HttpURLConnection connection = getConnection(OAuthConstants.GOOGLE_GET_TOKEN_URL, "POST", true);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        StringBuilder sb = new StringBuilder();

        sb.append("grant_type=authorization_code");
        sb.append("&client_id=" + googleClientId);
        sb.append("&client_secret=" + googleClientSecret);
        sb.append("&code=" + code);
        sb.append("&redirect_uri=" + googleRedirectUri);
        bw.write(sb.toString());
        bw.flush();

        int responseCode = connection.getResponseCode();
        log.info("getToken response code : {}", responseCode);

        String result = getResultString(connection.getInputStream());

        JsonParser jsonParser = new JacksonJsonParser();

        Map<String, Object> map = jsonParser.parseMap(result);
        String accessToken = (String) map.get("access_token");
        String idToken = (String) map.get("id_token");
        getUserInfo(accessToken);
        return "";
    }

    private void getUserInfo(String accessToken) throws IOException {
        String url = OAuthConstants.GOOGLE_GET_USERINFO_URL;
        HttpURLConnection connection = getConnection(url, "GET", true);

        connection.setRequestProperty("Authorization", "Bearer " + accessToken);

        int responseCode = connection.getResponseCode();
        log.info("getToken response code : {}", responseCode);

        String result = getResultString(connection.getInputStream());

        JsonParser jsonParser = new JacksonJsonParser();

        Map<String, Object> map = jsonParser.parseMap(result);
        String name = (String)map.get("name");
        String providerId = (String)map.get("id");
        System.out.println(name);

        User user = userService.retrieveUserWithProviderAndProviderId("google", providerId);
        if (user == null) {
            // 회원가입
            System.out.println("null");
        }else {
            // 로그인
            System.out.println("not null");
        }

//        userService.saveUser(user);

    }

    private HttpURLConnection getConnection(String string_url, String requestMethod, boolean doOutPut) throws IOException {
        URL url = new URL(string_url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(requestMethod);
        connection.setDoOutput(doOutPut);

        return connection;
    }

    private String getResultString(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        StringBuilder result = new StringBuilder();

        while ((line = br.readLine()) != null) {
            result.append(line);
        }

        return result.toString();
    }
}
