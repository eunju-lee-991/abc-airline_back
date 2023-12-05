package com.abcairline.abc.dto.user;

import com.abcairline.abc.domain.User;
import lombok.Data;

import java.time.format.DateTimeFormatter;

@Data
public class SimpleUserDto {
    private Long id;
    private String email;
    private String name;
    private String imageUrl;
    private boolean socialLoginYn;
    private String providerId;
    private String provider;
    private String lastAccessDate;

    public SimpleUserDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.imageUrl = user.getImageUrl();
        this.socialLoginYn = user.isSocialLoginYn();
        this.providerId = user.getProviderId();
        this.provider = user.getProvider();
        this.lastAccessDate = user.getLastAccessDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
 }
}
