package com.pm.billing.controller;

import com.pm.billing.dto.BillRequestDto;
import com.pm.billing.dto.BillResponseDto;
import com.pm.billing.dto.UpdateBillRequestDto;
import com.pm.billing.entity.Patient;
import com.pm.billing.service.BillService;
import com.pm.billing.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bill")
public class BillController {

    private final BillService billService;
    private final PatientService patientService;


    public BillController(BillService billService, PatientService patientService) {
        this.patientService = patientService;
        this.billService = billService;
    }

    //Get All Patients exists in a billing db
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients(){
        List<Patient> allPatients = patientService.getAllPatients();
        return new ResponseEntity<>(allPatients, HttpStatus.OK);
    }

    // Create a new bill
    @PostMapping
    public ResponseEntity<BillResponseDto> createBill(@Valid @RequestBody BillRequestDto billRequest) {
        BillResponseDto savedBill = billService.addBill(billRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBill);
    }

    // Get all bills for a patient
    @GetMapping("/patient/{email}")
    public ResponseEntity<List<BillResponseDto>> getBillsByPatientEmail(@PathVariable String email) {
        List<BillResponseDto> bills = billService.getAllBillsByEmail(email);
        return ResponseEntity.ok(bills);
    }

    // Get a single bill by ID
    @GetMapping("/{id}")
    public ResponseEntity<BillResponseDto> getBillById(@PathVariable String id) {
        BillResponseDto bill = billService.getBillById(id);
        return ResponseEntity.ok(bill);
    }

    // Update bill by email or ID (email is unusual — ID preferred)
    @PutMapping("/{id}")
    public ResponseEntity<BillResponseDto> updateBill(@PathVariable String id, @Valid @RequestBody UpdateBillRequestDto updateBillRequest) {
        BillResponseDto updatedBill = billService.updateBill(updateBillRequest, id);
        return ResponseEntity.ok(updatedBill);
    }
}
