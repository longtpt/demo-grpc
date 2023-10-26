package io.enotion.demo.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonNaming(value= PropertyNamingStrategy.KebabCaseStrategy.class)
public class GrpcChannelProperties {
    private String loadBalancingPolicy = "round_robin";
    Boolean enableKeepAlive = false;

    @DurationUnit(ChronoUnit.SECONDS)
    Duration keepAliveTime = Duration.of(5, ChronoUnit.MINUTES);

    // timeout for keepAlives ping request
    @DurationUnit(ChronoUnit.SECONDS)
    Duration keepAliveTimeout = Duration.of(20, ChronoUnit.SECONDS);

    Boolean keepAliveWithoutCalls = false;

    Integer maxInboundMessageSize = null;

    Integer maxInboundMetadataSize = null;
}
