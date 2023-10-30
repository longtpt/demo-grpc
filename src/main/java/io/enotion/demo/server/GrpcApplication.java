package io.enotion.demo.server;

import io.grpc.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@RequiredArgsConstructor
@EnableConfigurationProperties
public class GrpcApplication implements CommandLineRunner {
    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = SpringApplication.run(GrpcApplication.class, args);
        context.getBean(Server.class).awaitTermination();
    }

    @Override
    public void run(String... args) throws Exception {
    }

}
