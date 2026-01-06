package com.ecommerce.user.controller;

import com.ecommerce.user.dto.*;
import com.ecommerce.user.mapper.UserMapper;
import com.ecommerce.user.service.KeyCloakAdminService;
import com.ecommerce.user.service.UsersService;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.String;

@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
@Slf4j
public class UsersController {

    private final UsersService usersService;
    private final KeyCloakAdminService keyCloakAdminService;
//    private final static Logger logger = LoggerFactory.getLogger(UsersController.class);

    @GetMapping
    public ResponseEntity<GetAllUsersDto> getAllUsers() {
        var users = usersService.findAll();
        var response = new GetAllUsersDto(users, users.size());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<CreateUserResponseDto> createUser(@RequestBody CreateUserRequestDto dto) {
        String token = keyCloakAdminService.getAccessToken();
        keyCloakAdminService.createUser(token, dto);
        var savedUser = usersService.save(UserMapper.mapFromCreateUserRequestDtoToUser(dto));
        savedUser.setKeycloakId(savedUser.getKeycloakId());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.mapToCreateUserResponseDto(savedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        usersService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully!");
    }

    @GetMapping("/validate/{id}")
    public ResponseEntity<ValidateUserResponseDto> existsUser(@PathVariable String id) {
        String userId = usersService.findById(id).getId();
        var responseDto = ValidateUserResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("User found")
                .data(userId)
                .build();
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}")
    @RateLimiter(name="rateLimiter", fallbackMethod = "getUserByIdFallback")
    public ResponseEntity<GetUserResponseDto> getUserById(@PathVariable String id) {
        var user = usersService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(UserMapper.mapToGetUserResponseDto(user));
    }

    public ResponseEntity<GetUserResponseDto> getUserByIdFallback(String id, RequestNotPermitted exception) {
        log.warn("Rate limit exceeded for getUserById({}), reason: {}", id, exception.toString());

        // return a HTTP 429 (Too Many Requests)
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(null);
    }


    @PutMapping("/{id}")
    public ResponseEntity<UpdateUserResponseDto> updateUser(@PathVariable String id, @RequestBody UpdateUserRequestDto dto) {
        var user = usersService.update(id, dto.username(), dto.email(), dto.phone());
        return ResponseEntity.status(HttpStatus.OK).body(UserMapper.mapToUpdateUserResponseDto(user));
    }

    @PatchMapping("/{userId}/addresses/{addressId}")
    public ResponseEntity<AddressResponseDto> updateAddress(@PathVariable String userId, @PathVariable String addressId, @RequestBody AddressRequestDto dto) {
        var address = usersService.updateAddress(userId, addressId, dto.city(), dto.country(), dto.street(), dto.zipcode());
        return ResponseEntity.status(HttpStatus.OK).body(UserMapper.toAddressResponseDto(address));
    }

    @GetMapping("/{userId}/addresses")
    public ResponseEntity<AddressResponseDto> getAddress(
            @PathVariable String userId
    ) {
        var address = usersService.findAddressByUserId(userId);
        return ResponseEntity.ok(UserMapper.toAddressResponseDto(address));
    }

}
