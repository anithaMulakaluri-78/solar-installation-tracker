package com.solartracker.dto.request;
import jakarta.validation.constraints.NotNull; import lombok.Getter; import lombok.Setter; import java.math.BigDecimal;
@Getter @Setter public class LocationRequest { @NotNull private BigDecimal latitude; @NotNull private BigDecimal longitude; private BigDecimal accuracyMeters; }
