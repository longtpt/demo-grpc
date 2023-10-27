package io.enotion.demo;

import com.google.protobuf.Timestamp;
import io.enotion.demo.server.services.ProductService;
import io.enotion.proto.product.ProductResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class PingGrpcServiceTest {
    @Autowired
    ProductService productService;

    @Test
    public void givenProduct_whenGetProduct_thenReturnProductResponse() {
        // given
        ProductResponse givenProduct = ProductResponse.newBuilder()
                .setId("1")
                .setName("Tra sua")
                .setPrice(1_000)
                .setCreatedDate(Timestamp.newBuilder().setSeconds(1_000))
                .build();
        productService.productRepository.put("1", givenProduct);

        // when
        ProductResponse result = productService.getProduct("1");

        // then
        assertEquals(result, givenProduct);
    }

    @Test
    void givenProduct_whenGetProduct_thenThrowProductNotFound() {
        // given
        ProductResponse givenProduct = ProductResponse.newBuilder()
                .setId("0")
                .setName("Tra sua")
                .setPrice(1_000)
                .setCreatedDate(Timestamp.newBuilder().setSeconds(1_000))
                .build();
        productService.productRepository.put("0", givenProduct);

        // when
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> {
            productService.getProduct("1");
        });

        // then
        assertEquals(runtimeException.getClass(), RuntimeException.class);
    }

}
