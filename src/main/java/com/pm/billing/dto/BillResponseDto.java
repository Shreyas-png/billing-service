package com.pm.billing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BillResponseDto {

    private String email;

    private BigDecimal amount;

    private String purpose;

    private LocalDateTime bill_date;

    private LocalDateTime due_date;
}
