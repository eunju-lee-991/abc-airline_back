package com.abcairline.abc.dto.auth;

import lombok.Data;

@Data
public class OauthUserInfo {
    private String providerId;
    private String provider;
    private String email;
    private String name;
    private String imageUrl;
}
