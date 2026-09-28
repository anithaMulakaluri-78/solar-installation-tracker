package com.solartracker.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter; import lombok.Setter;
@Getter @Setter
public class ConsumerRequest {
    @NotBlank private String consumerNumber;
    private String ulaApplicationId;
    @NotBlank private String name;
    private String mobile, alternateMobile, address, village, mandal, district, state, pincode, section, distribution, siteType, status;
}
