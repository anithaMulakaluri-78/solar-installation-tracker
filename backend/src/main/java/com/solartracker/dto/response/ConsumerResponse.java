package com.solartracker.dto.response;
import lombok.*; import java.time.LocalDateTime;
@Getter @Builder @AllArgsConstructor
public class ConsumerResponse {
    private Long id; private String consumerNumber, ulaApplicationId, name, mobile, alternateMobile, address, village, mandal, district, state, pincode, section, distribution, siteType, status; private LocalDateTime createdAt, updatedAt;
}
