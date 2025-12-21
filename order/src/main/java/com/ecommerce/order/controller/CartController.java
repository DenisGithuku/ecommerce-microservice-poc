package com.ecommerce.order.controller;

import com.ecommerce.order.dto.AddItemRequestDto;
import com.ecommerce.order.dto.CartResponseDto;
import com.ecommerce.order.dto.CreateCartRequestDto;
import com.ecommerce.order.dto.CreateCartResponseDto;
import com.ecommerce.order.entity.Cart;
import com.ecommerce.order.entity.Order;
import com.ecommerce.order.mapper.CartMapper;
import com.ecommerce.order.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/api/orders")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<CreateCartResponseDto> createCart(@RequestBody CreateCartRequestDto dto) {
        Cart cart = cartService.createCart(dto.userId());
        return ResponseEntity.ok(CartMapper.toCreateCartResponseDto(cart));
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartResponseDto> addItem(
            @PathVariable Long cartId,
            @RequestBody AddItemRequestDto dto) {

        Cart cart = cartService.addItem(cartId, dto.productId(), dto.quantity());
        return ResponseEntity.ok(CartMapper.toCartResponseDto(cart));
    }

    @DeleteMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<CartResponseDto> removeItem(
            @PathVariable Long cartId,
            @PathVariable Long itemId) {

        Cart cart = cartService.removeItem(cartId, itemId);
        return ResponseEntity.ok(CartMapper.toCartResponseDto(cart));
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartResponseDto> getCart(@PathVariable Long cartId) {
        return ResponseEntity.ok(
                CartMapper.toCartResponseDto(cartService.getCart(cartId))
        );
    }

    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<CartResponseDto> clearCart(@PathVariable Long cartId) {
        return ResponseEntity.ok(
                CartMapper.toCartResponseDto(cartService.clearCart(cartId))
        );
    }

    @PostMapping("/{cartId}/checkout")
    public ResponseEntity<String> checkout(@PathVariable Long cartId) {
        Order order = cartService.checkout(cartId);
        return ResponseEntity.ok("Order placed successfully! Order ID: " + order.getId());
    }
}
