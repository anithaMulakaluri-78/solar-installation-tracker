package com.solartracker.dto.response;
import lombok.*; import java.math.BigDecimal; import java.time.LocalDateTime;
@Getter @Builder @AllArgsConstructor public class PhotoResponse { private Long id; private String photoType, fileName, filePath, status; private BigDecimal latitude, longitude; private LocalDateTime capturedAt, uploadedAt; }
