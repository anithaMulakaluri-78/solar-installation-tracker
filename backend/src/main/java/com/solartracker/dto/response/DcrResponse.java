package com.solartracker.dto.response;
import com.solartracker.entity.enums.DcrStatus; import lombok.*; import java.time.LocalDateTime;
@Getter @Builder @AllArgsConstructor public class DcrResponse { private Long id; private Long installationId; private String dcrNumber; private DcrStatus dcrStatus; private String remarks; private LocalDateTime submittedAt, verifiedAt; }
