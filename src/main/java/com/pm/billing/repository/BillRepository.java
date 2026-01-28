package com.pm.billing.repository;

import com.pm.billing.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BillRepository extends JpaRepository<Bill, UUID> {
    List<Bill> findByPatientId(String patientId);
    Bill findByBillId(UUID billId);
}
