package io.enotion.demo.server.services;

import com.google.protobuf.Empty;
import com.google.protobuf.StringValue;
import io.enotion.proto.product.CreateProductRequest;
import io.enotion.proto.product.GetProductsRequest;
import io.enotion.proto.product.ProductResponse;
import io.enotion.proto.product.ProductServiceGrpc;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductGrpcServiceImpl extends ProductServiceGrpc.ProductServiceImplBase {
    private final ProductService productService;

    @Override
    public void createProduct(CreateProductRequest request, StreamObserver<StringValue> responseObserver) {
//        String productId = productService.createProduct(request);
        String productId = "sample id";
        responseObserver.onNext(StringValue.of(productId));
        log.info("Do some task");
        try {
            Thread.sleep(2_000);
        } catch (InterruptedException e) {
            responseObserver.onError(e);
        }
        responseObserver.onCompleted();
        log.info("create product completed");
    }

    @Override
    public void getProducts(GetProductsRequest request, StreamObserver<ProductResponse> responseObserver) {
        productService.getProducts(request)
                .forEach(p -> {
                    try {
                        Thread.sleep(1_000);
                    } catch (InterruptedException e) {
                        responseObserver.onError(e);
                    }
                    responseObserver.onNext(p);
                });
        responseObserver.onCompleted();
        log.info("get products completed");
    }

    @Override
    public void getProduct(StringValue request, StreamObserver<ProductResponse> responseObserver) {
        try {
            responseObserver.onNext(productService.getProduct(request.getValue()));
        } catch (RuntimeException e) {
            throw new StatusRuntimeException(Status.NOT_FOUND
                    .withDescription(e.getMessage())
                    .withCause(e));
        }
        responseObserver.onCompleted();
        log.info("get product completed");
    }

    @Override
    public void deleteProduct(StringValue request, StreamObserver<Empty> responseObserver) {
        productService.deleteProduct(request.getValue());
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
        log.info("delete product completed");
    }

}
