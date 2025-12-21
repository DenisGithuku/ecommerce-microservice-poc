package com.ecommerce.user.repository;

import com.ecommerce.user.entity.Address;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AddressesRepository extends MongoRepository<Address, String> {

}
