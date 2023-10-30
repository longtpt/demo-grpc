package io.enotion.demo.server.services;

import com.google.protobuf.Empty;
import com.google.protobuf.Int32Value;
import io.enotion.proto.PingResponse;
import io.enotion.proto.PingServiceGrpcGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
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

    @Override
    public StreamObserver<Int32Value> stackPing(StreamObserver<Int32Value> responseObserver) {
        return new StreamObserver<>() {
            Integer counter = 0;

            @Override
            public void onNext(Int32Value int32Value) {
                log.info("\nOn Next {}", int32Value.getValue());
                counter += int32Value.getValue();
            }

            @Override
            public void onError(Throwable throwable) {
                log.error(throwable.getMessage(), throwable);
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(Int32Value.newBuilder()
                        .setValue(counter).build());
                responseObserver.onCompleted();
            }
        };
    }
}