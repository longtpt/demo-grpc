package io.enotion.demo.mockito;

import io.enotion.demo.server.services.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class BDDMockitoTest {
    @Mock
    ProductService productService;

    @Test
    public void testThrowException() {
//        given(productService.getProduct(any(String.class)))
//                .willReturn(ProductResponse.getDefaultInstance());
        // given
        given(productService.getProduct(any(String.class)))
                .willThrow(new RuntimeException());

        // when
        try {
            productService.getProduct("simple string");
            Assertions.fail("should throw exception");
        } catch (RuntimeException ex) {
        }

        // then
        then(productService).should(never()).createProduct(any());
    }

}
