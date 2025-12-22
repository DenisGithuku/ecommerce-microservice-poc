package com.ecommerce.order.service;

import com.ecommerce.order.entity.Cart;
import com.ecommerce.order.entity.CartItem;
import com.ecommerce.order.entity.Order;
import com.ecommerce.order.exception.DuplicateOrderException;
import com.ecommerce.order.exception.ResourceNotFoundException;
import com.ecommerce.order.productserviceclient.UserServiceClient;
import com.ecommerce.order.repository.CartItemRepository;
import com.ecommerce.order.repository.CartRepository;
import com.ecommerce.order.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final UserServiceClient userServiceClient;


    @CircuitBreaker(name = "userService")
    @Override
    public Cart createCart(String userId) {
        var response = userServiceClient.validateUser(userId);

        if (response.getStatus() == HttpStatus.NOT_FOUND.value()) {
            throw new ResourceNotFoundException("User with id: " + userId + " not found");
        }
        if (cartRepository.existsByUserId(userId)) {
            throw new RuntimeException("Cart for user: " + userId + " exists already");
        }
        Cart cart = Cart.builder()
                .userId(userId)
                .items(List.of())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        return cartRepository.save(cart);
    }

    @Override
    public Cart addItem(Long cartId, Long productId, Integer quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException(Cart.class, cartId.toString()));

        CartItem item = CartItem.builder()
                .productId(productId)
                .quantity(quantity)
                .cart(cart)
                .build();

        cart.getItems().add(item);
        cart.setUpdatedAt(Instant.now());

        cartItemRepository.save(item);
        return cartRepository.save(cart);
    }

    @Override
    public Cart removeItem(Long cartId, Long itemId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException(Cart.class, cartId.toString()));

        cart.getItems().removeIf(i -> i.getId().equals(itemId));

        cart.setUpdatedAt(Instant.now());
        cartRepository.save(cart);

        cartItemRepository.deleteById(itemId);

        return cart;
    }

    @Override
    public Cart getCart(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException(Cart.class, cartId.toString()));
    }

    @Override
    public Cart clearCart(Long cartId) {
        Cart cart = getCart(cartId);
        cart.getItems().clear();
        cartRepository.save(cart);
        return cart;
    }

    @Override
    public Order checkout(Long cartId) {
        Cart cart = getCart(cartId);

        if (cart.isCheckedOut()) throw new RuntimeException("Cart already checked out");

        List<Long> productIds =
                cart.getItems().stream().map(CartItem::getProductId).toList();

        Order order = Order.builder()
                .userId(cart.getUserId())
                .productIds(productIds)
                .createdAt(Instant.now())
                .build();

        orderRepository.save(order);

        cart.setCheckedOut(true);
        cartRepository.save(cart);

        return order;
    }
}
