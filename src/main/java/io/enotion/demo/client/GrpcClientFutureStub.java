package io.enotion.demo.client;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.protobuf.StringValue;
import io.enotion.proto.PingServiceGrpcGrpc;
import io.enotion.proto.product.CreateProductRequest;
import io.enotion.proto.product.ProductServiceGrpc;
import io.grpc.Channel;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;

@Slf4j
public class GrpcClientFutureStub extends GrpcClient {
    private final ProductServiceGrpc.ProductServiceFutureStub futureProductStub;
    private final PingServiceGrpcGrpc.PingServiceGrpcFutureStub pingStub;
    private final Scanner s = new Scanner(System.in);

    public GrpcClientFutureStub(Channel channel) {
        futureProductStub = ProductServiceGrpc.newFutureStub(channel);
        pingStub = PingServiceGrpcGrpc.newFutureStub(channel);
    }

    void createProduct() throws ExecutionException, InterruptedException {
        System.out.println("###### START FUNCTION ######: Create Product");
        CreateProductRequest request = CreateProductRequest.newBuilder()
                .setName(inputString("Please enter product name"))
                .setPrice(inputNumber("Please enter product price"))
                .build();
        ListenableFuture<StringValue> productIdFuture = futureProductStub.createProduct(request);
        log.info("Created product: {} \n", productIdFuture.get());
        System.out.println("###### END FUNCTION ######: Create Product");
    }

    void getProductsStream() {
        System.out.println("######: List of Product - Future stub not support asynchronous call");
    }

    void getProduct() {
        System.out.println("###### START FUNCTION ######: - Get Product");
        try {
//            ProductResponse product = futureProductStub.getProduct(StringValue.of(inputString("Enter product id:")));
//            printProduct(null, product);
        } catch (StatusRuntimeException e) {
            System.out.printf("RPC failed: %s %s \n", e.getStatus(), e.getMessage());
        }
        System.out.println("###### END FUNCTION ######: - Get Product");
    }

    void deleteProduct() {
        System.out.println("###### START FUNCTION ######: - Delete Product");
        futureProductStub.deleteProduct(StringValue.of(inputString("Enter product id:")));
        System.out.println("###### END FUNCTION ######: - Delete Product");
    }

    @Override
    void ping() {
        System.out.println("######: Ping - Blocking stub not support Bidirectional streaming");
    }

    @Override
    void stackPing() {
        System.out.println("######: Stack Ping - Blocking stub not support Bidirectional streaming");
    }
}
