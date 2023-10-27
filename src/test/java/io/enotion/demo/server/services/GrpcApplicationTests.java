package io.enotion.demo.server.services;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class GrpcApplicationTests {
    @MockBean
    ProductService productService;

    @Test
    void contextLoads() {
        productService.getClass();
    }

}
