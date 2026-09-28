package com.solartracker.dto.response;
import lombok.*; import java.time.LocalDateTime;
@Getter @Builder @AllArgsConstructor public class VerificationResponse { private Long id; private String verificationStatus, verificationRemarks; private UserSummaryResponse verifiedBy; private LocalDateTime verifiedAt; }
