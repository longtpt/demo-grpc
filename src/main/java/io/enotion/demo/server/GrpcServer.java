package io.enotion.demo.server;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.enotion.demo.server.services.PingGrpcServiceImpl;
import io.enotion.demo.server.services.ProductGrpcServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import static java.util.concurrent.TimeUnit.SECONDS;
@Slf4j
@Component
@RequiredArgsConstructor
public class GrpcServer {
    private final PingGrpcServiceImpl pingService;
    private final ProductGrpcServiceImpl productService;
    private final GrpcServerProperties properties;
    private Server server;

    private Executor cachedThreadPool() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setDaemon(true)
                .setNameFormat("grpc-default-executor-%d")
                .build();
        return Executors.newCachedThreadPool(threadFactory);
    }

    private Executor forkJoinThreadPool() {
        return Executors.newWorkStealingPool(2);
    }

    private Executor fixedThreadPool() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setDaemon(true)
                .setNameFormat("grpc-default-executor-%d")
                .build();
        return Executors.newFixedThreadPool(4, threadFactory);
    }

    private Executor getExecutor() {
        switch (GrpcServerProperties.ExecutorType.valueOf(properties.getExecutorType())) {
            case FIXED:
                return fixedThreadPool();
            case FORK_JOIN:
                return forkJoinThreadPool();
            default:
                return cachedThreadPool();
        }
    }

    @Bean
    public Server server() throws InterruptedException, IOException {
        Server server = ServerBuilder.forPort(9090)
                .addService(pingService)
                .addService(productService)
                .executor(getExecutor())
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
