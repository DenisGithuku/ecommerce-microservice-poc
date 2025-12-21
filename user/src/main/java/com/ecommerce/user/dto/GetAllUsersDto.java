package com.ecommerce.user.dto;

import com.ecommerce.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAllUsersDto {
    private List<User> users;
    private Integer size;
}
