package com.example;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

@EnableZuulProxy
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class ProductServiceEdgeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceEdgeApplication.class, args);
    }
}

@RestController()
@RequestMapping("/products")
class ProductApi {
    private ProductResource productResource;

    @Autowired
    public ProductApi(ProductResource productResource) {
        this.productResource = productResource;
    }

    @GetMapping("/names")
    public Collection<String> names() {
        return this.productResource.getAll()
                                    .getContent()
                                    .stream()
                                    .map(Product::getName)
                                    .collect(toList());
    }
}

@FeignClient("product-service")
interface ProductResource {
    @GetMapping("/products")
    Resources<Product> getAll();
}

@Data
class Product {
    private String name;
}