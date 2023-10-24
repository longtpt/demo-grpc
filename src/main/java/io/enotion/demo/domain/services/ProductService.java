package io.enotion.demo.domain.services;

import com.google.protobuf.Timestamp;
import io.enotion.proto.product.CreateProductRequest;
import io.enotion.proto.product.GetProductsRequest;
import io.enotion.proto.product.ProductResponse;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
@Service
public class ProductService {
    Map<String, ProductResponse> productRepository = new HashMap<>();

    public String createProduct(CreateProductRequest request) {
        String productId = UUID.randomUUID().toString();
        productRepository.put(productId, ProductResponse.newBuilder()
                .setId(productId)
                .setName(request.getName())
                .setPrice(request.getPrice())
                .setCreatedDate(Timestamp.newBuilder().setSeconds(Instant.now().getEpochSecond()))
                .build());
        return productId;
    }

    public ProductResponse getProduct(String productId) {
        ProductResponse product = productRepository.get(productId);
        if (Objects.isNull(product)) {
            throw new RuntimeException("Product " + productId + " not found");
        }
        return product;
    }

    public Stream<ProductResponse> getProducts(GetProductsRequest request) {
        return productRepository.values().stream()
                .sorted(new Comparator<ProductResponse>() {
                    @Override
                    public int compare(ProductResponse o1, ProductResponse o2) {
                        return Long.compare(o1.getCreatedDate().getSeconds(), o2.getCreatedDate().getSeconds());
                    }
                })
                .skip(request.getOffset())
                .limit(request.getLimit());
    }

    public void deleteProduct(String productId) {
        productRepository.remove(productId);
    }
}
