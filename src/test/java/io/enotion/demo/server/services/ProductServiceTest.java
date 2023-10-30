package io.enotion.demo.server.services;

import com.google.protobuf.Timestamp;
import io.enotion.proto.product.ProductResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProductServiceTest {
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
        Assertions.assertEquals(result, givenProduct);
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
        RuntimeException runtimeException = Assertions.assertThrows(RuntimeException.class, () -> {
            productService.getProduct("1");
        });

        // then
        Assertions.assertEquals(runtimeException.getClass(), RuntimeException.class);
    }

}
