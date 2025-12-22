package com.ecommerce.order.userserviceclient;

import com.ecommerce.order.dto.ValidateUserResponseDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface UserServiceClient {

    @GetExchange("/api/users/validate/{userId}")
    ValidateUserResponseDto validateUser(@PathVariable String userId);

}
