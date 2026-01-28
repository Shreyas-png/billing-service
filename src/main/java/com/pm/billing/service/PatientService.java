package com.pm.billing.service;

import com.pm.billing.entity.Patient;
import com.pm.billing.exception.ResourceNotFound;
import com.pm.billing.proto.BillingRequest;
import com.pm.billing.proto.BillingResponse;
import com.pm.billing.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PatientService {

    //Injecting repository bean
    private final PatientRepository patientRepository;
    public PatientService(PatientRepository patientRepository){
        this.patientRepository = patientRepository;
    }

    //Service Method to save patient in patient db
    public BillingResponse savePatient(BillingRequest billingRequest){

        //Building patient entity
        Patient patient = Patient.builder()
                .patient_id(UUID.fromString(billingRequest.getPatientId()))
                .email(billingRequest.getEmail())
                .status(billingRequest.getStatus())
                .build();

        //Saving patient to db
        Patient savedPatient = patientRepository.save(patient);

        //Building GRPC Response and returning the same
        return BillingResponse.newBuilder()
                .setEmail(savedPatient.getEmail())
                .setPatientId(savedPatient.getPatient_id().toString())
                .setStatus(savedPatient.getStatus())
                .build();
    }

    //Service Method to get patient by email
    public String getPatientIdByEmail(String email){
        Patient patient = patientRepository.findByEmail(email);
        if(patient == null) throw new ResourceNotFound("Patient not found with email %s".formatted(email));

        return patient.getPatient_id().toString();

    }

    //Service Method to get patient by patient ID
    public String getEmailByPatientId(String patientId){
        return patientRepository.findById(UUID.fromString(patientId)).get().getEmail();
    }

    //Service method to get all patients exists db
    public List<Patient> getAllPatients(){
        return patientRepository.findAll();
    }
}
