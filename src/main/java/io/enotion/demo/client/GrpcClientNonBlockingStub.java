package io.enotion.demo.client;

import com.google.protobuf.Empty;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import io.enotion.proto.PingResponse;
import io.enotion.proto.PingServiceGrpcGrpc;
import io.enotion.proto.product.CreateProductRequest;
import io.enotion.proto.product.GetProductsRequest;
import io.enotion.proto.product.ProductResponse;
import io.enotion.proto.product.ProductServiceGrpc;
import io.grpc.Channel;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.IntStream;

@Slf4j
public class GrpcClientNonBlockingStub extends GrpcClient {
    private final ProductServiceGrpc.ProductServiceStub blockingProductStub;
    private final PingServiceGrpcGrpc.PingServiceGrpcStub pingStub;

    public GrpcClientNonBlockingStub(Channel channel) {
        blockingProductStub = ProductServiceGrpc.newStub(channel);
        pingStub = PingServiceGrpcGrpc.newStub(channel);
    }

    void createProduct() {
        System.out.println("###### START FUNCTION ######: Create Product");
        CreateProductRequest request = CreateProductRequest.newBuilder()
                .setName(inputString("Please enter product name"))
                .setPrice(inputNumber("Please enter product price"))
                .build();
        try {
            blockingProductStub.createProduct(request, new StreamObserver<>() {
                @Override
                public void onNext(StringValue productId) {
                    log.info("\n Created product: {} \n", productId.getValue());
                }

                @Override
                public void onError(Throwable throwable) {
                    System.err.println(throwable.getMessage());
                }

                @Override
                public void onCompleted() {
                    log.info("\n onCompleted: Create Product");
                }
            });
        } catch (StatusRuntimeException e) {
            System.out.printf("RPC failed: %s %s", e.getStatus(), e.getMessage());
        }
        System.out.println("###### END FUNCTION ######: Create Product");
    }

    void getProductsStream() {
        System.out.println("###### START FUNCTION ######: - List of Product - ServerSideStreaming");
        GetProductsRequest request = GetProductsRequest.newBuilder()
                .setOffset(inputNumber("Offset: "))
                .setLimit(inputNumber("Limit: "))
                .build();
        System.out.printf("REQUEST - offset: %s limit: %s \n", request.getOffset(), request.getLimit());
        try {
            blockingProductStub.getProducts(request, new StreamObserver<>() {
                @Override
                public void onNext(ProductResponse product) {
                    printProduct(0, product);
                }

                @Override
                public void onError(Throwable throwable) {
                    log.error(throwable.getMessage(), throwable);
                }

                @Override
                public void onCompleted() {
                    log.info("get products complete");
                }
            });
        } catch (StatusRuntimeException e) {
            System.out.printf("RPC failed: %s %s \n", e.getStatus(), e.getMessage());
        }
        System.out.println("###### END FUNCTION ######: - List of Product - ServerSideStreaming");
    }

    void getProduct() {
        System.out.println("###### START FUNCTION ######: - Get Product");
        blockingProductStub.getProduct(StringValue.of(inputString("Enter product id:")), new StreamObserver<ProductResponse>() {
            @Override
            public void onNext(ProductResponse product) {
                printProduct(null, product);
            }

            @Override
            public void onError(Throwable throwable) {
                log.error(throwable.getMessage(), throwable);
            }

            @Override
            public void onCompleted() {
                log.info("get product complete");
            }
        });
        System.out.println("###### END FUNCTION ######: - Get Product");
    }

    void deleteProduct() {
        System.out.println("###### START FUNCTION ######: - Delete Product");
        blockingProductStub.deleteProduct(StringValue.of(inputString("Enter product id:")), new StreamObserver<Empty>() {
            @Override
            public void onNext(Empty empty) {
                log.info("on Next");
            }

            @Override
            public void onError(Throwable throwable) {
                log.error(throwable.getMessage(), throwable);
            }

            @Override
            public void onCompleted() {
                log.info("delete product complete");
            }
        });
        System.out.println("###### END FUNCTION ######: - Delete Product");
    }

    @Override
    void ping() {
        StreamObserver<Empty> pingObserver = pingStub.ping(new StreamObserver<>() {
            @Override
            public void onNext(PingResponse pingResponse) {
                log.info("\n Server response: {}", pingResponse);
            }

            @Override
            public void onError(Throwable throwable) {
                log.error(throwable.getMessage(), throwable);
            }

            @Override
            public void onCompleted() {
                log.info("\nOn completed");
            }
        });
        pingObserver.onNext(Empty.getDefaultInstance());
        pingObserver.onNext(Empty.getDefaultInstance());
        pingObserver.onCompleted();
    }

    @Override
    void stackPing() {
        StreamObserver<Int32Value> pingObserver = pingStub.stackPing(new StreamObserver<>() {
            @Override
            public void onNext(Int32Value int32Value) {
                log.info("\n Server response: {}", int32Value.getValue());
            }

            @Override
            public void onError(Throwable throwable) {
                log.error(throwable.getMessage(), throwable);
            }

            @Override
            public void onCompleted() {
                log.info("\nOn completed");
            }
        });
        IntStream.range(1, 20).parallel().flatMap(i -> {
            pingObserver.onNext(Int32Value.newBuilder().setValue(i).build());
            return IntStream.of(i);
        }).toArray();
        pingObserver.onCompleted();
    }
}

