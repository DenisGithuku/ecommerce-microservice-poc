package com.ecommerce.user.dto;


public record AddressRequestDto(String city,
                                String street,
                                String country,
                                String zipcode
) {
}

