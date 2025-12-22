package com.ecommerce.order.productserviceclient;

import com.ecommerce.order.dto.ValidateUserResponseDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface ProductServiceClient {

    @GetExchange("/api/products/{id}")
    ValidateUserResponseDto getProduct(@PathVariable Long id);

}

