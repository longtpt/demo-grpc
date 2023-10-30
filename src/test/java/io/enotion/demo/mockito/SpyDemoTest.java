package io.enotion.demo.mockito;

import io.enotion.demo.server.services.ProductService;
import io.enotion.proto.product.ProductResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SpyDemoTest {
    @Spy
    Map<String, ProductResponse> productRepository = new HashMap<>();

    @InjectMocks
    ProductService productService;

    @Test
    public void given_whenAdd2Element_thenListSizeGrowUp() {
        // given

        // when
        productRepository.put("one",null);
        productRepository.put("two", null);

        // then
        verify(productRepository).put("one", null);
        verify(productRepository).put("two", null);
        Assertions.assertEquals(2, productRepository.size());
    }
}
