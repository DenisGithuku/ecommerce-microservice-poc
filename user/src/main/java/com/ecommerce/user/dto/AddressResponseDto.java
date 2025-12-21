package com.ecommerce.user.dto;

public record AddressResponseDto(
    String id,
    String city,
    String street,
    String country,
    String zipcode
){}
