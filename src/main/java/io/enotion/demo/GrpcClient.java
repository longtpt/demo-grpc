package io.enotion.demo;

import com.google.protobuf.StringValue;
import io.enotion.proto.product.CreateProductRequest;
import io.enotion.proto.product.GetProductsRequest;
import io.enotion.proto.product.ProductResponse;
import io.enotion.proto.product.ProductServiceGrpc;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@Slf4j
public class GrpcClient {
    private final ProductServiceGrpc.ProductServiceBlockingStub blockingProductStub;
    private final Scanner s = new Scanner(System.in);

    public GrpcClient(Channel channel) {
        blockingProductStub = ProductServiceGrpc.newBlockingStub(channel);
    }

    public static void main(String[] args) throws InterruptedException {
        String host = "localhost:9090";
        ManagedChannel channel = ManagedChannelBuilder.forTarget(host)
                .usePlaintext()
                .build();
        try {
            new GrpcClient(channel).run();
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    void createProduct() {
        System.out.println("###### START FUNCTION ######: Create Product");
        CreateProductRequest request = CreateProductRequest.newBuilder()
                .setName(inputString("Please enter product name"))
                .setPrice(inputNumber("Please enter product price"))
                .build();
        try {
            String productId = blockingProductStub.createProduct(request).getValue();
            System.out.printf("Created product: %s \n", productId);
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
            Iterator<ProductResponse> products = blockingProductStub.getProducts(request);
            for (int i = 0; products.hasNext(); i++) {
                ProductResponse product = products.next();
                printProduct(i, product);
            }
        } catch (StatusRuntimeException e) {
            System.out.printf("RPC failed: %s %s \n", e.getStatus(), e.getMessage());
        }
        System.out.println("###### END FUNCTION ######: - List of Product - ServerSideStreaming");
    }

    void getProduct() {
        System.out.println("###### START FUNCTION ######: - Get Product");
        try{
            ProductResponse product = blockingProductStub.getProduct(StringValue.of(inputString("Enter product id:")));
            printProduct(null, product);
        }
        catch (StatusRuntimeException e){
            System.out.printf("RPC failed: %s %s \n", e.getStatus(), e.getMessage());
        }
        System.out.println("###### END FUNCTION ######: - Get Product");
    }

    void deleteProduct() {
        System.out.println("###### START FUNCTION ######: - Delete Product");
        blockingProductStub.deleteProduct(StringValue.of(inputString("Enter product id:")));
        System.out.println("###### END FUNCTION ######: - Delete Product");
    }

    void printProduct(Integer i, ProductResponse product) {
        System.out.printf("RESPONSE: %s \n id: %s \n name: %s \n price: %s \n createdDate: %s \n \n", i,
                product.getId(), product.getName(), product.getPrice(),
                LocalDateTime.ofEpochSecond(product.getCreatedDate().getSeconds(),
                        product.getCreatedDate().getNanos(), ZoneOffset.of("+07:00")));
    }

    Integer inputNumber(String message) {
        System.out.println(message);
        try {
            return Integer.parseInt(s.nextLine());
        } catch (Exception e) {
            System.out.println("Please input a number");
            return inputNumber(message);
        }
    }

    String inputString(String message) {
        System.out.println(message);
        return s.nextLine();
    }

    public void run() {
        String guide = " 1. Create product;\n 2. Get product stream;\n 3. Get product;\n 4. Delete product;\n 0. Exit;";
        while (true) {
            System.out.println(guide);
            String line = s.nextLine();
            switch (line) {
                case "0": {
                    return;
                }
                case "1": {
                    createProduct();
                    break;
                }
                case "2": {
                    getProductsStream();
                    break;
                }
                case "3": {
                    getProduct();
                    break;
                }
                case "4": {
                    deleteProduct();
                    break;
                }
                default: {
                    System.out.println("Please select option from 0..4");
                }
            }
        }
    }

}
