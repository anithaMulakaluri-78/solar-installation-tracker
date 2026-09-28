package com.solartracker.dto.request;
import com.solartracker.entity.enums.DcrStatus; import lombok.Getter; import lombok.Setter;
@Getter @Setter public class DcrRequest { private String dcrNumber; private DcrStatus dcrStatus; private String remarks; }
