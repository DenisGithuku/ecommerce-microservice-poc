package com.ecommerce.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customerRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("product-service", route -> route.path("/products/**").filters(f -> f.rewritePath("/products(?<segment>/?.*)", "/api/products${segment}")).uri("lb://product-service"))
                .route("user-service", route -> route.path("/users/**").filters(f ->f.rewritePath("/users(?<segment>/?.*)", "/api/users${segment}")).uri("lb://user-service"))
                .route("order-service", route -> route.path("/orders/**").filters(f -> f.rewritePath("/orders(?<segment>/?.*)", "/api/orders${segment}")).uri("lb://order-service"))
                .route("eureka-server", route -> route.path("/eureka/main").filters(f -> f.rewritePath("eureka/main", "/")).uri("http://localhost:8761"))
                .route("eureka-server-static", route -> route.path("/eureka/**").uri("http://localhost:8761"))
                .build();
    }
}
