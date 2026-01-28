package com.pm.billing.service;

import com.pm.billing.dto.BillRequestDto;
import com.pm.billing.dto.BillResponseDto;
import com.pm.billing.dto.UpdateBillRequestDto;
import com.pm.billing.entity.Bill;
import com.pm.billing.entity.BillStatus;
import com.pm.billing.exception.BadRequest;
import com.pm.billing.exception.ResourceNotFound;
import com.pm.billing.repository.BillRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BillService {

    //injecting BillRepository and PatientService beans
    private final BillRepository billRepository;
    private final PatientService patientService;

    public BillService(BillRepository billRepository, PatientService patientService){
        this.billRepository = billRepository;
        this.patientService = patientService;
    }

    //Method to get All bills of a patient
    public List<BillResponseDto> getAllBillsByEmail(String email){

        //get patient id from email with the help of patient service
        String patientId = patientService.getPatientIdByEmail(email);
        if(patientId == null) throw new ResourceNotFound("Patient with mail %s does not exist".formatted(email));

        // get existing patient bills by patient id
        List<Bill> patientBills = billRepository.findByPatientId(patientId);
        if(patientBills.isEmpty()) throw new ResourceNotFound("No Bills exist for patient with mail %s".formatted(email));

        //Convert List<Bill> to List<BillResponseDto> and return the same
        List<BillResponseDto> billsDto = new ArrayList<>();
        for(Bill bill : patientBills){
            BillResponseDto billDto = BillResponseDto.builder()
                    .email(email)
                    .bill_date(bill.getBill_date())
                    .amount(bill.getAmount())
                    .due_date(bill.getDue_date())
                    .purpose(bill.getPurpose())
                    .build();
            billsDto.add(billDto);
        }
        return billsDto;
    }

    //Get Bill by bill ID
    public BillResponseDto getBillById(String billId){

            //Finding bill by id(querying the db)
            Bill bill = billRepository.findById(UUID.fromString(billId))
                    .orElseThrow(() -> new ResourceNotFound("No Bill Found with ID %s".formatted(billId)));


            //getting email of a patient with the help of patient id
            //this line doesn't have exception because if bill exist then email must exist
            String patientEmail = patientService.getEmailByPatientId(bill.getPatientId());

            // building BillResponseDto and returning the same
            return BillResponseDto.builder()
                    .purpose(bill.getPurpose())
                    .bill_date(bill.getBill_date())
                    .due_date(bill.getDue_date())
                    .amount(bill.getAmount())
                    .email(patientEmail)
                    .build();
        }

    //method to add bill to db
    @Transactional
    public BillResponseDto addBill(BillRequestDto billRequest){

        //get patient id from email with the help of patient service
        String patientId = patientService.getPatientIdByEmail(billRequest.getEmail());
        if(patientId == null) throw new ResourceNotFound("Patient with mail %s does not exist".formatted(billRequest.getEmail()));

        //building bill entity
        Bill bill = Bill.builder()
                .amount(billRequest.getAmount())
                .purpose(billRequest.getPurpose())
                .patientId(patientId)
                .build();

        //saving to db
        Bill savedBill = billRepository.save(bill);

        // building BillResponseDto and returning the same
        return BillResponseDto.builder()
                .amount(savedBill.getAmount())
                .email(billRequest.getEmail())
                .purpose(savedBill.getPurpose())
                .bill_date(savedBill.getBill_date())
                .due_date(savedBill.getDue_date())
                .build();
    }

    @Transactional
    public BillResponseDto updateBill(UpdateBillRequestDto updateBillRequest, String billId){

        //getting Bill entity from billID
        Bill bill = billRepository.findById(UUID.fromString(billId))
                .orElseThrow(() -> new ResourceNotFound("No Bill Found with ID %s".formatted(billId)));

        //updating bill entity to save to db
        if(updateBillRequest.getPaymentMode() != null && !updateBillRequest.getPaymentMode().isBlank())
            bill.setPayment_mode(updateBillRequest.getPaymentMode());

        if(updateBillRequest.getStatus() != null && !updateBillRequest.getStatus().isBlank()){
            if(updateBillRequest.getStatus().equals("PAID"))
                bill.setStatus(BillStatus.PAID);
            else if(updateBillRequest.getStatus().equals("CANCELLED"))
                bill.setStatus(BillStatus.CANCELLED);
        }

        //saving updated entity to db
        Bill newBill = billRepository.save(bill);

        //fetching patient email from patient id
        //No Exception handler because if bill exist then patient also exist
        String email = patientService.getEmailByPatientId(newBill.getPatientId());

        //building BillResponseDto and returning the same
        return BillResponseDto.builder()
                .email(email)
                .amount(newBill.getAmount())
                .bill_date(newBill.getBill_date())
                .due_date(newBill.getDue_date())
                .purpose(newBill.getPurpose())
                .build();
    }
}