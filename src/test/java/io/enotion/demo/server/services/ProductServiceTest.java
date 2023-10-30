package io.enotion.demo.server.services;

import com.google.protobuf.Timestamp;
import io.enotion.proto.product.CreateProductRequest;
import io.enotion.proto.product.GetProductsRequest;
import io.enotion.proto.product.ProductResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@SpringBootTest
public class ProductServiceTest {

    @Spy
    Map<String, ProductResponse> productRepository = new HashMap<>();
    @Captor
    ArgumentCaptor<String> productIdCaptor;

    @InjectMocks
    ProductService productService;

    @Test
    public void given_whenCreateProduct_thenReturnProductId() {
        // given

        // when
        CreateProductRequest request = CreateProductRequest.newBuilder()
                .setName("product_name")
                .setPrice(1_000)
                .build();
        String createdProductId = productService.createProduct(request);
        verify(productRepository).put(productIdCaptor.capture(), any());

        // then
        assertEquals(productIdCaptor.getValue(), createdProductId);
    }

    @Test
    public void givenProduct_whenGetProduct_thenReturnProductResponse() {
        // given
        ProductResponse givenProduct = ProductResponse.newBuilder()
                .setId("1")
                .setName("Tra sua")
                .setPrice(1_000)
                .setCreatedDate(Timestamp.newBuilder().setSeconds(1_000))
                .build();
        given(productRepository.get("1")).willReturn(givenProduct);

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

    @Test
    void givenProducts_whenGetProducts_thenReturnProducts() {
        // given
        ProductResponse givenProduct0 = ProductResponse.newBuilder()
                .setId("0")
                .setName("Tra sua")
                .setPrice(1_000)
                .setCreatedDate(Timestamp.newBuilder().setSeconds(1_000))
                .build();
        ProductResponse givenProduct1 = ProductResponse.newBuilder()
                .setId("1")
                .setName("Tra sua")
                .setPrice(1_000)
                .setCreatedDate(Timestamp.newBuilder().setSeconds(1_000))
                .build();
        given(productRepository.values()).willReturn(List.of(
                givenProduct0, givenProduct1
        ));
        // when
        Stream<ProductResponse> products = productService.getProducts(GetProductsRequest.newBuilder()
                .setOffset(0)
                .setLimit(10)
                .build());
        // then
        var productsArr = products.toArray(ProductResponse[]::new);
        assertEquals(productsArr[0], givenProduct0);
        assertEquals(productsArr[1], givenProduct1);
    }

    @Test
    void givenProduct_whenDeleteIt_then() {
        // given
        ProductResponse givenProduct = ProductResponse.newBuilder()
                .setId("0")
                .setName("Tra sua")
                .setPrice(1_000)
                .setCreatedDate(Timestamp.newBuilder().setSeconds(1_000))
                .build();
        given(productRepository.remove(anyString())).willReturn(givenProduct);

        // when
        productService.deleteProduct("0");

        // then
        then(productRepository)
                .should(times(1))
                .remove("0");

    }
}
