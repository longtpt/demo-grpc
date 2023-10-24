package io.enotion.demo.application.services;

import com.google.protobuf.Empty;
import io.enotion.proto.PingResponse;
import io.enotion.proto.PingServiceGrpcGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PingGrpcServiceImpl extends PingServiceGrpcGrpc.PingServiceGrpcImplBase {
    @Override
    public StreamObserver<Empty> ping(StreamObserver<PingResponse> responseObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(Empty empty) {
                responseObserver.onNext(PingResponse.newBuilder().setPong(true).build());
            }

            @Override
            public void onError(Throwable throwable) {
                log.warn("error:{}", throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}