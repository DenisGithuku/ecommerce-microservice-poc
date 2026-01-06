package com.ecommerce.user.mapper;

import com.ecommerce.user.entity.Address;
import com.ecommerce.user.entity.Role;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.dto.*;

import java.time.Instant;

public class UserMapper {
    public static User mapFromCreateUserRequestDtoToUser(CreateUserRequestDto dto) {
        return User.builder()
                .username(dto.username())
                .email(dto.email())
                .phone(dto.phone())
                .role(Role.CUSTOMER)
                .address(dto.address())
                .build();
    }

    public static CreateUserResponseDto mapToCreateUserResponseDto(User user) {
        return new CreateUserResponseDto(user.getId(), user.getUsername(), user.getEmail());
    }

    public static CreateUserRequestDto mapToCreateUserRequestDto(User user) {
        return new CreateUserRequestDto(
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getPhone(),
                user.getRole().name(),
                user.getAddress(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public static GetUserResponseDto mapToGetUserResponseDto(User user) {
        return new GetUserResponseDto(user.getId(), user.getUsername(), user.getEmail(), user.getPhone(), user.getRole(), user.getAddress());
    }

    public static UpdateUserResponseDto mapToUpdateUserResponseDto(User user) {
        return new UpdateUserResponseDto(user.getId(), user.getUsername(), user.getEmail());
    }

    public static User mapFromUpdateUserRequestDtoToUser(UpdateUserRequestDto dto) {
        return User.builder()
                .email(dto.email())
                .username(dto.username())
                .phone(dto.phone())
                .build();
    }

    public static Address toAddress(AddressRequestDto dto) {
        return Address.builder()
                .city(dto.city())
                .country(dto.country())
                .street(dto.street())
                .zipcode(dto.zipcode())
                .build();
    }

    public static AddressResponseDto toAddressResponseDto(Address address) {
        return new AddressResponseDto(
                address.getId(),
                address.getCity(),
                address.getStreet(),
                address.getCountry(),
                address.getZipcode()
        );
    }
}

