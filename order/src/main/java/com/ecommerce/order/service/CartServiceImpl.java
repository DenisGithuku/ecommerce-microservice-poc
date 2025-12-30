package com.ecommerce.order.service;

import com.ecommerce.order.entity.Cart;
import com.ecommerce.order.entity.CartItem;
import com.ecommerce.order.entity.Order;
import com.ecommerce.order.exception.DuplicateCartException;
import com.ecommerce.order.exception.ResourceNotFoundException;
import com.ecommerce.order.exception.ServiceUnavailableException;
import com.ecommerce.order.productserviceclient.ProductServiceClient;
import com.ecommerce.order.userserviceclient.UserServiceClient;
import com.ecommerce.order.repository.CartItemRepository;
import com.ecommerce.order.repository.CartRepository;
import com.ecommerce.order.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final UserServiceClient userServiceClient;
    private final ProductServiceClient productServiceClient;
    private final RabbitTemplate rabbitTemplate;

    @CircuitBreaker(name = "userService", fallbackMethod = "createCartFallback")
    @Override
    public Cart createCart(String userId) {
        var response = userServiceClient.validateUser(userId);

        if (response.getStatus() == HttpStatus.NOT_FOUND.value()) {
            throw new ResourceNotFoundException("User with id: " + userId + " not found");
        }
        if (cartRepository.existsByUserId(userId)) {
            throw new DuplicateCartException(userId);
        }
        Cart cart = Cart.builder()
                .userId(userId)
                .items(List.of())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        return cartRepository.save(cart);
    }

    public Cart createCartFallback(String userId, Throwable throwable) throws Throwable {
        // Ignore business exceptions; only handle real failures
        if (throwable instanceof ResourceNotFoundException || throwable instanceof DuplicateCartException) {
            throw throwable;
        }

        log.error("CircuitBreaker fallback for createCart, userId {} ", userId, throwable);
        throw new ServiceUnavailableException("Could not complete the request! Please try again later!");
    }


    @CircuitBreaker(name = "productService", fallbackMethod = "addToCartFallback")
    @Retry(name = "retryBreaker", fallbackMethod = "addToCartFallback")
    @Override
    public Cart addItem(Long cartId, Long productId, Integer quantity) {
        // Verify that item exists
        var response = productServiceClient.getProduct(productId);

        if (response.getStatus() == HttpStatus.NOT_FOUND.value()) {
            throw new ResourceNotFoundException("Could not find a product with id: " + productId);
        }
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

    public Cart addToCartFallback(Long cartId, Long productId, Integer quantity, Throwable throwable) {
        log.error("Fallback triggered for cartId={}, productId={}", cartId, productId, throwable);

        // convert any exception to your custom one
        if (!(throwable instanceof ResourceNotFoundException)) {
            throw new ServiceUnavailableException("Could not complete the request! Please try again later!", throwable);
        }
        throw (ResourceNotFoundException) throwable;
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
        rabbitTemplate.convertAndSend("order.exchange",
                "order.tracking",
                Map.of("orderId", cart.getId(), "status", "CREATED"));
        cartRepository.save(cart);

        return order;
    }
}
