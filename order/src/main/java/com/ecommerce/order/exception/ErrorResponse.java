package com.ecommerce.order.exception;

import lombok.*;

@Builder
@AllArgsConstructor
@Getter
public class ErrorResponse {
    private int status;
    private String message;
    private Long timestamp;
    private String path;
}