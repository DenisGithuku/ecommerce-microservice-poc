package com.ecommerce.user.dto;

import com.ecommerce.user.entity.Address;
import com.ecommerce.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetUserResponseDto {
    private String id;
    private String username;
    private String email;
    private String phone;
    private Role role;
    private Address address;
}
