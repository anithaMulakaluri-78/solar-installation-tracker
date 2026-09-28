package com.solartracker.dto.request;
import jakarta.validation.constraints.NotBlank; import lombok.Getter; import lombok.Setter;
@Getter @Setter public class VerificationRequest { @NotBlank private String verificationStatus; private String verificationRemarks; }
