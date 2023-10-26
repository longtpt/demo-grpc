package io.enotion.demo.server;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "grpc.server")
public class GrpcServerProperties {
    Duration shutdownGracePeriod = Duration.of(30, ChronoUnit.SECONDS);
    boolean enableKeepAlive = false;
    Duration keepAliveTime = Duration.of(2, ChronoUnit.HOURS);
    Duration keepAliveTimeout = Duration.of(20, ChronoUnit.SECONDS);
    boolean permitKeepAliveWithoutCalls = false;
    @DurationUnit(ChronoUnit.SECONDS)
    Duration maxConnectionIdle = null;

    @DurationUnit(ChronoUnit.SECONDS)
    Duration maxConnectionAge = null;

    @DataSizeUnit(DataUnit.BYTES)
    DataSize maxInboundMessageSize = null;

    @DataSizeUnit(DataUnit.BYTES)
    DataSize maxInboundMetadataSize = null;

    String executorType;

    public enum ExecutorType {
        CACHED, FIXED, FORK_JOIN
    }
}
