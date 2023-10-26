package io.enotion.demo.server;

import io.grpc.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@RequiredArgsConstructor
@EnableConfigurationProperties
public class GrpcApplication implements CommandLineRunner {
    private final Server grpcServer;

    public static void main(String[] args) {
        SpringApplication.run(GrpcApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        grpcServer.awaitTermination();
    }

}
