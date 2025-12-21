package com.ecommerce.order.mapper;

import com.ecommerce.order.dto.CartItemDto;
import com.ecommerce.order.dto.CartResponseDto;
import com.ecommerce.order.dto.CreateCartResponseDto;
import com.ecommerce.order.entity.Cart;
import com.ecommerce.order.entity.CartItem;

public class CartMapper {

    public static CartResponseDto toCartResponseDto(Cart cart) {
        return new CartResponseDto(
                cart.getId(),
                cart.getUserId(),
                cart.getItems().stream()
                        .map(item -> new CartItemDto(
                                item.getId(),
                                item.getProductId(),
                                item.getQuantity()))
                        .toList(),
                cart.isCheckedOut()
        );
    }

    public static CreateCartResponseDto toCreateCartResponseDto(Cart cart) {
        return new CreateCartResponseDto(
                cart.getId(),
                cart.getUserId(),
                cart.getItems().stream().map(CartItem::getProductId).toList()
        );
    }
}
