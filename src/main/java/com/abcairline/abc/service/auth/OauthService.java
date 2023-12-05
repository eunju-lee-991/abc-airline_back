package com.abcairline.abc.service.auth;

import com.abcairline.abc.controller.constant.OAuthConstants;
import com.abcairline.abc.controller.constant.ProviderType;
import com.abcairline.abc.dto.auth.OauthUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class OauthService {

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

    public String getAccessToken(String code, ProviderType providerType) throws IOException {

        HttpURLConnection connection = null;
        BufferedWriter bw = null;
        StringBuilder sb = new StringBuilder();

        if(providerType == ProviderType.GOOGLE){
            connection = getConnection(OAuthConstants.GOOGLE_GET_TOKEN_URL, "POST", true);
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=" + googleClientId);
            sb.append("&client_secret=" + googleClientSecret);
            sb.append("&code=" + code);
            sb.append("&redirect_uri=" + googleRedirectUri);
        } else if (providerType == ProviderType.NAVER) {
            connection = getConnection(OAuthConstants.NAVER_GET_TOKEN_URL, "POST", true);
            String state = URLEncoder.encode(naverRedirectUri, StandardCharsets.UTF_8);
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=" + naverClientId);
            sb.append("&client_secret=" + naverClientSecret);
            sb.append("&code=" + code);
            sb.append("&state=" + state);
        } else if (providerType == ProviderType.KAKAO) {
            connection = getConnection(OAuthConstants.KAKAO_GET_TOKEN_URL, "POST", true);
            String state = URLEncoder.encode(naverRedirectUri, StandardCharsets.UTF_8);
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=" + kakaoClientId);
            sb.append("&redirect_uri=" + KaKaoRedirectUri);
            sb.append("&code=" + code);
        }

        bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        bw.write(sb.toString());
        bw.flush();

        int responseCode = connection.getResponseCode();
        String accessToken = "";

        if(responseCode == HttpURLConnection.HTTP_OK){
            String result = getResultString(connection.getInputStream());
            JsonParser jsonParser = new JacksonJsonParser();
            Map<String, Object> map = jsonParser.parseMap(result);
            accessToken = (String) map.get("access_token");
        } else {
            // throw exception
        }

        return accessToken;
    }

    public OauthUserInfo getUserInformation(String accessToken, ProviderType providerType) throws IOException {
        String url = "";
        BufferedWriter bw = null;

        if(providerType == ProviderType.GOOGLE){
            url = OAuthConstants.GOOGLE_GET_USERINFO_URL;
       } else if (providerType == ProviderType.NAVER) {
            url = OAuthConstants.NAVER_GET_USERINFO_URL;
        } else if (providerType == ProviderType.KAKAO){
            String[] propertyKeys = {"kakao_account.profile", "kakao_account.name", "kakao_account.email", "kakao_account.age_range", "kakao_account.birthday", "kakao_account.gender"};
            StringBuilder sb = new StringBuilder();
            for (String key : propertyKeys) {
                sb.append("property_keys").append(key).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
            url = OAuthConstants.KAKAO_GET_USERINFO_URL + "?" + sb.toString();
        }

        HttpURLConnection connection = getConnection(url, "GET", true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);

        int responseCode = connection.getResponseCode();
        OauthUserInfo userInfo = null;

        if (responseCode == HttpURLConnection.HTTP_OK) {
            String result = getResultString(connection.getInputStream());
            JsonParser jsonParser = new JacksonJsonParser();
            userInfo = new OauthUserInfo();
            Map<String, Object> map = jsonParser.parseMap(result);
            String name = "";
            String email = "";
            String providerId = "";
            String imageUrl = "";

            if(providerType == ProviderType.NAVER){
                Map<String, String> profileMap = (LinkedHashMap) map.get("response");
                providerId = profileMap.get("id");
                name = profileMap.get("name");
                email = profileMap.get("email");
                imageUrl = profileMap.get("profile_image");
            } else if (providerType == ProviderType.KAKAO) {
                providerId = String.valueOf(map.get("id"));
                Map<String, String> properties = (LinkedHashMap) map.get("properties");
                name = properties.get("nickname");
                imageUrl = properties.get("profile_image");
                Map<String, String> kakao_account = (LinkedHashMap) map.get("kakao_account");
                email = kakao_account.get("email");
            } else if (providerType == ProviderType.GOOGLE) {
                providerId = (String) map.get("id");
                name = (String)map.get("name");
                email = (String) map.get("email");
                imageUrl = (String) map.get("picture");
            }

            userInfo.setProvider(providerType.name());
            userInfo.setProviderId(providerId);
            userInfo.setName(name);
            userInfo.setEmail(email);
            userInfo.setImageUrl(imageUrl);
            log.info("=============getUserInformation==============");
            log.info("providerType {}, map {}", providerType, userInfo);

        }else {
            // throw some exception
        }

        return userInfo;
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
