package io.enotion.demo.client;

import com.google.protobuf.StringValue;
import io.enotion.proto.PingServiceGrpcGrpc;
import io.enotion.proto.product.CreateProductRequest;
import io.enotion.proto.product.GetProductsRequest;
import io.enotion.proto.product.ProductResponse;
import io.enotion.proto.product.ProductServiceGrpc;
import io.grpc.Channel;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;

@Slf4j
public class GrpcClientBlockingStub extends GrpcClient {
    private final ProductServiceGrpc.ProductServiceBlockingStub productStub;
    private final PingServiceGrpcGrpc.PingServiceGrpcBlockingStub pingStub;

    public GrpcClientBlockingStub(Channel channel) {
        productStub = ProductServiceGrpc.newBlockingStub(channel);
        pingStub = PingServiceGrpcGrpc.newBlockingStub(channel);
    }

    @Override
    public void createProduct() {
        System.out.println("###### START FUNCTION ######: Create Product");
        CreateProductRequest request = CreateProductRequest.newBuilder()
                .setName(inputString("Please enter product name"))
                .setPrice(inputNumber("Please enter product price"))
                .build();
        try {
            String productId = productStub.createProduct(request).getValue();
            log.info("\nCreated product: {} \n", productId);
        } catch (StatusRuntimeException e) {
            System.out.printf("RPC failed: %s %s", e.getStatus(), e.getMessage());
        }
        System.out.println("###### END FUNCTION ######: Create Product");
    }

    @Override
    public void getProductsStream() {
        System.out.println("###### START FUNCTION ######: - List of Product - ServerSideStreaming");
        GetProductsRequest request = GetProductsRequest.newBuilder()
                .setOffset(inputNumber("Offset: "))
                .setLimit(inputNumber("Limit: "))
                .build();
        System.out.printf("REQUEST - offset: %s limit: %s \n", request.getOffset(), request.getLimit());
        try {
            Iterator<ProductResponse> products = productStub.getProducts(request);
            for (int i = 0; products.hasNext(); i++) {
                ProductResponse product = products.next();
                printProduct(i, product);
            }
        } catch (StatusRuntimeException e) {
            System.out.printf("RPC failed: %s %s \n", e.getStatus(), e.getMessage());
        }
        System.out.println("###### END FUNCTION ######: - List of Product - ServerSideStreaming");
    }

    @Override
    public void getProduct() {
        System.out.println("###### START FUNCTION ######: - Get Product");
        try {
            ProductResponse product = productStub.getProduct(StringValue.of(inputString("Enter product id:")));
            printProduct(null, product);
        } catch (StatusRuntimeException e) {
            System.out.printf("RPC failed: %s %s \n", e.getStatus(), e.getMessage());
        }
        System.out.println("###### END FUNCTION ######: - Get Product");
    }

    @Override
    public void deleteProduct() {
        System.out.println("###### START FUNCTION ######: - Delete Product");
        productStub.deleteProduct(StringValue.of(inputString("Enter product id:")));
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
