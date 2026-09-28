package com.solartracker.dto.response;
import lombok.*; import java.math.BigDecimal; import java.time.LocalDateTime;
@Getter @Builder @AllArgsConstructor public class InverterResponse { private Long id; private String inverterSerialNumber, inverterMake, inverterModel; private BigDecimal inverterCapacityKw; private LocalDateTime createdAt; }
