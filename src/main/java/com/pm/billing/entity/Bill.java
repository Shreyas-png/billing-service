package com.pm.billing.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bill")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bill {

    @Id
    @GeneratedValue
    @Column(name = "bill_id")
    private UUID billId;

    @Column(unique = true, name = "patient_id")
    private String patientId;

    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    private String purpose;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private BillStatus status = BillStatus.PENDING;

    private String payment_mode;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime bill_date;

    private LocalDateTime due_date;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updated_at;

    public void setDefaultDueDate() {
        if (due_date == null) {
            due_date = LocalDateTime.now().plusWeeks(1);
        }
    }
}
