package io.enotion.demo.mockito;

import io.enotion.demo.server.services.ProductService;
import io.enotion.proto.product.CreateProductRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LenientDemoTest {
    @Mock
    ProductService productService;

    @BeforeEach
    public void before() {
        CreateProductRequest request = CreateProductRequest
                .newBuilder()
                .setName("new name")
                .setPrice(100)
                .build();
        Mockito.lenient().when(productService.createProduct(request))
                .thenReturn("new id");
    }

    @Test
    public void testCreateProduct() {
        // given

        // when
        CreateProductRequest request = CreateProductRequest
                .newBuilder()
                .setName("new name")
                .setPrice(100)
                .build();
        String productId = productService.createProduct(request);

        // then
        Assertions.assertEquals("new id", productId);
    }

    @Test
    public void getProduct() {
        // given
        // when
        productService.deleteProduct("other id");
        // then
    }
}
