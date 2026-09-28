package com.solartracker.dto.request;
import jakarta.validation.constraints.NotBlank; import lombok.Getter; import lombok.Setter; import java.math.BigDecimal;
@Getter @Setter public class InverterRequest { @NotBlank private String inverterSerialNumber; private String inverterMake, inverterModel; private BigDecimal inverterCapacityKw; }
