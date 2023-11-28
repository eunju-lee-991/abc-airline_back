package com.abcairline.abc.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AncillaryService {
    private String inflightMeal; // 나중에 enum으로 변경
    private String luggage; // 나중에 enum으로 변경
    private String wifi;
}
