package io.enotion.demo.mockito;

import com.google.protobuf.StringValue;
import io.enotion.demo.server.services.ProductGrpcServiceImpl;
import io.enotion.demo.server.services.ProductService;
import io.enotion.proto.product.CreateProductRequest;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CaptorDemoTest {
    @Mock
    ProductService productService;

    @InjectMocks
    ProductGrpcServiceImpl productGrpcService;

    @Captor
    ArgumentCaptor<CreateProductRequest> argCaptor;

    @Test
    public void given_whenCreateProduct_thenReturnProductId() {
        // given
        var request = CreateProductRequest.newBuilder()
                .setName("new product")
                .build();
        Mockito.when(productService.createProduct(request)).thenReturn("new id");

        // when
        productGrpcService.createProduct(request, new StreamObserver<>() {
            @Override
            public void onNext(StringValue stringValue) {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {

            }
        });

        // then
        Mockito.verify(productService).createProduct(argCaptor.capture());
        Assertions.assertAll(
                "New product is:",
                () -> Assertions.assertEquals("new product", argCaptor.getValue().getName()),
                () -> Assertions.assertEquals(0, argCaptor.getValue().getPrice())
        );
    }
}
