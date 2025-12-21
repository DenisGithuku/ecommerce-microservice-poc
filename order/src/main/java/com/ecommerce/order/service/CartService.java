package com.ecommerce.order.service;

import com.ecommerce.order.entity.Cart;
import com.ecommerce.order.entity.Order;

public interface CartService {

    Cart createCart(String userId);

    Cart addItem(Long cartId, Long productId, Integer quantity);

    Cart removeItem(Long cartId, Long itemId);

    Cart getCart(Long cartId);

    Cart clearCart(Long cartId);

    Order checkout(Long cartId);
}
