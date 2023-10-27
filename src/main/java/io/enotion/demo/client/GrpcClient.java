package io.enotion.demo.client;

import io.enotion.proto.product.ProductResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class GrpcClient {
    protected final Scanner scanner = new Scanner(System.in);

    abstract void createProduct() throws ExecutionException, InterruptedException;

    abstract void getProductsStream();

    abstract void getProduct();

    abstract void deleteProduct();

    abstract void ping();

    abstract void stackPing();

    void printProduct(Integer i, ProductResponse product) {
        log.info("\n RESPONSE: {} \n id: {} \n name: {} \n price: {} \n createdDate: {} \n \n", i,
                product.getId(), product.getName(), product.getPrice(),
                LocalDateTime.ofEpochSecond(product.getCreatedDate().getSeconds(),
                        product.getCreatedDate().getNanos(), ZoneOffset.of("+07:00")));
    }

    Integer inputNumber(String message) {
        System.out.println(message);
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Please input a number");
            return inputNumber(message);
        }
    }

    String inputString(String message) {
        System.out.println(message);
        return scanner.nextLine();
    }


    public void run() throws ExecutionException, InterruptedException {
        String guide = " 1. Create product;\n 2. Get product stream;\n 3. Get product;\n 4. Delete product;\n 5. Ping;\n 6.Stack ping;\n 0. Exit;";
        while (true) {
            System.out.println(guide);
            String line = scanner.nextLine();
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
                case "5": {
                    ping();
                    break;
                }
                case "6": {
                    stackPing();
                    break;
                }
                default: {
                    System.out.println("Please select option from 0..6");
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String host = "localhost:9090";
        var properties = new GrpcPropertiesProvider()
                .getProperties();
        log.debug(properties.toString());
        ManagedChannelBuilder channelBuilder = ManagedChannelBuilder.forTarget(host)
                .usePlaintext()
                .defaultLoadBalancingPolicy(properties.getLoadBalancingPolicy())
                .keepAliveWithoutCalls(properties.getKeepAliveWithoutCalls());
        if (properties.getEnableKeepAlive()) {
            channelBuilder.keepAliveTime(properties.getKeepAliveTime().getSeconds(), TimeUnit.SECONDS);
            channelBuilder.keepAliveTimeout(properties.getKeepAliveTimeout().getSeconds(), TimeUnit.SECONDS);
        }
        if (Objects.nonNull(properties.getMaxInboundMessageSize())) {
            channelBuilder.maxInboundMessageSize(properties.getMaxInboundMessageSize());
        }
        if (Objects.nonNull(properties.getMaxInboundMetadataSize())) {
            channelBuilder.maxInboundMetadataSize(properties.getMaxInboundMetadataSize());
        }
        ManagedChannel channel = channelBuilder.build();
        try {
            new GrpcClientNonBlockingStub(channel);
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
