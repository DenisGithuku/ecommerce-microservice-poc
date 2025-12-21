package com.ecommerce.user.service;

import com.ecommerce.user.entity.Address;
import com.ecommerce.user.entity.User;

import java.util.List;

public interface UsersService {
    User save(User user);

    List<User> findAll();

    User update(String id, String username, String email, String phone);

    User findById(String id);

    void deleteById(String id);

    Address addAddressToUser(String userId, Address address);

    void deleteAddress(String id);

    Address findAddressByUserId(String id);

    List<Address> getAddresses();

    Address updateAddress(String userId, String addressId, String city, String country, String street, String zipcode);
}