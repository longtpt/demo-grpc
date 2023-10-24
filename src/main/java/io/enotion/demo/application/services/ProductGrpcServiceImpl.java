package io.enotion.demo.application.services;

import com.google.protobuf.Empty;
import com.google.protobuf.StringValue;
import io.enotion.demo.domain.services.ProductService;
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
        String productId = productService.createProduct(request);
        responseObserver.onNext(StringValue.of(productId));
        responseObserver.onCompleted();
    }

    @Override
    public void getProducts(GetProductsRequest request, StreamObserver<ProductResponse> responseObserver) {
        productService.getProducts(request)
                .forEach(responseObserver::onNext);
        responseObserver.onCompleted();
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
    }

    @Override
    public void deleteProduct(StringValue request, StreamObserver<Empty> responseObserver) {
        productService.deleteProduct(request.getValue());
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

}
