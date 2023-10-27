package io.enotion.demo.server;

import com.google.common.annotations.VisibleForTesting;
import io.grpc.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GrpcInterceptor implements ServerInterceptor {
    @VisibleForTesting
    static final Metadata.Key<String> CUSTOM_HEADER_KEY =
            Metadata.Key.of("custom_server_header_key", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            final Metadata metadata,
            ServerCallHandler<ReqT, RespT> next) {
        log.info("header received from client:" + metadata);
        return next.startCall(new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(call) {
            @Override
            public void sendHeaders(Metadata responseHeaders) {
                responseHeaders.put(CUSTOM_HEADER_KEY, "Initial Metadata");
                super.sendHeaders(responseHeaders);
            }

            @Override
            public void close(Status status, Metadata trailers) {
                trailers.put(CUSTOM_HEADER_KEY, "Trailer Metadata");
                super.close(status, trailers);
            }
        }, metadata);
    }
}
