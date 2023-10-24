package io.enotion.demo;

import io.enotion.demo.application.services.PingGrpcServiceImpl;
import io.enotion.demo.application.services.ProductGrpcServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
@Component
@RequiredArgsConstructor
public class GrpcServer {
    private final PingGrpcServiceImpl pingService;
    private final ProductGrpcServiceImpl productService;
    private Server server;

    @Bean
    public Server server() throws InterruptedException, IOException {
        Server server = ServerBuilder.forPort(9090)
                .addService(pingService)
                .addService(productService)
                .build();
        log.info("Starting grpc server...");
        server.start();
        Runtime.getRuntime()
                .addShutdownHook(new Thread() {
                    @Override
                    public void run() {
                        System.err.println("shutting down server");
                        try {
                            GrpcServer.this.stop();
                        } catch (InterruptedException e) {
                            e.printStackTrace(System.err);
                        }
                        System.err.println("server down");
                    }
                });
        log.info("Server stared");
        this.server = server;
        return server;
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown()
                    .awaitTermination(30, SECONDS);
        }
    }
}
