package com.pm.billing.grpc;

import com.pm.billing.proto.BillingRequest;
import com.pm.billing.proto.BillingResponse;
import com.pm.billing.proto.BillingServiceGrpc;
import com.pm.billing.service.PatientService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class BillingGrpcService extends BillingServiceGrpc.BillingServiceImplBase {

    //injecting patient service bean
    private final PatientService patientService;
    public BillingGrpcService(PatientService patientService){
        this.patientService = patientService;
    }

    //creating logger object
    private static final Logger log = LoggerFactory.getLogger(BillingGrpcService.class);

    @Override
    public void createBillingAccount(BillingRequest request, StreamObserver<BillingResponse> responseObserver) {

        log.info("createBillingAccount request received {}", request.toString());

        BillingResponse billingResponse = patientService.savePatient(request);

        responseObserver.onNext(billingResponse);
        responseObserver.onCompleted();
    }
}
