package com.ecommerce.user.service;


import com.ecommerce.user.entity.Address;
import com.ecommerce.user.entity.Role;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.exception.DuplicateEmailException;
import com.ecommerce.user.exception.ResourceNotFoundException;
import com.ecommerce.user.repository.AddressesRepository;
import com.ecommerce.user.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final AddressesRepository addressesRepository;

    
    public UsersServiceImpl(UsersRepository usersRepository, AddressesRepository addressesRepository) {
        this.usersRepository = usersRepository;
        this.addressesRepository = addressesRepository;
    }

    @Override
    public User save(User user) {
        if (usersRepository.existsByEmailIgnoreCase(user.getEmail()))
            throw new DuplicateEmailException(user.getEmail());
        var address = addressesRepository.save(user.getAddress());
        user.setAddress(address);
        return usersRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return usersRepository.findAll();
    }

    @Override
    public User update(String id, String username, String email, String phone) {
        var savedUser = usersRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Could not find user with those details!"));

        if (usersRepository.existsByEmailIgnoreCase(email) && !email.equalsIgnoreCase(savedUser.getEmail())) {
            throw new DuplicateEmailException(email);
        }

        savedUser.setUsername(username);
        savedUser.setEmail(email);
        savedUser.setPhone(phone);
        return usersRepository.save(savedUser);
    }

    @Override
    public User findById(String id) {
        return usersRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(User.class, id));
    }


    @Override
    public void deleteById(String id) {
        if (!usersRepository.existsById(id)) {
            throw new ResourceNotFoundException(User.class, id);
        }
        usersRepository.deleteById(id);
    }

    @Override
    public Address addAddressToUser(String userId, Address address) {
        var savedUser = usersRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(User.class, userId));

        savedUser.setAddress(address);
        usersRepository.save(savedUser);
        return address;
    }

    @Override
    public void deleteAddress(String id) {
        if (addressesRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException(Address.class, id);
        }
        addressesRepository.deleteById(id);
    }

    @Override
    public Address findAddressByUserId(String id) {
        return usersRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Address.class, id)).getAddress();
    }

    @Override
    public List<Address> getAddresses() {
        return addressesRepository.findAll();
    }

    @Override
    public Address updateAddress(String userId, String addressId, String city, String country, String street, String zipcode) {
        var savedUser = usersRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(User.class, userId));

        var savedAddress = savedUser.getAddress();

        if (savedAddress == null || !savedAddress.getId().equals(addressId)) {
            throw new ResourceNotFoundException(Address.class, addressId);
        }

        if (city != null && !city.isBlank()) {
            savedAddress.setCity(city);
        }
        if (country != null && !country.isBlank()) {
            savedAddress.setCountry(country);
        }
        if (street != null && !street.isBlank()) {
            savedAddress.setStreet(street);
        }
        if (zipcode != null && !zipcode.isBlank()) {
            savedAddress.setZipcode(zipcode);
        }

        usersRepository.save(savedUser);
        return savedAddress;
    }
}
