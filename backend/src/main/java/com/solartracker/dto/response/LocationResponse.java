package com.solartracker.dto.response;
import lombok.*; import java.math.BigDecimal; import java.time.LocalDateTime;
@Getter @Builder @AllArgsConstructor public class LocationResponse { private Long id; private BigDecimal latitude, longitude, accuracyMeters; private UserSummaryResponse capturedBy; private LocalDateTime capturedAt; }
