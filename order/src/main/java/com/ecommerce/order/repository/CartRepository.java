package com.ecommerce.order.repository;

import com.ecommerce.order.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    boolean existsByUserId(String userId);
}

