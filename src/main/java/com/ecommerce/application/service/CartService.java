package com.ecommerce.application.service;

import com.ecommerce.application.model.persistence.Cart;
import com.ecommerce.application.model.persistence.repositories.CartRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.IntStream;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserService userService;
    private final ItemService itemService;

    public CartService(CartRepository cartRepository, UserService userService, ItemService itemService) {
        this.cartRepository = cartRepository;
        this.userService = userService;
        this.itemService = itemService;
    }


    public Optional<Cart> modifyCart(String username, Long itemId, Integer quantity, boolean addItems) {
        var user = userService.findByUsername(username);
        if(user.isEmpty()) {
            return Optional.empty();
        }

        var item = itemService.findById(itemId);
        if(item.isEmpty()) {
            return Optional.empty();
        }

        Cart cart = user.get().getCart();
        IntStream.range(0, quantity)
                .forEach(i -> {
                    if (addItems) {
                        cart.addItem(item.get());
                    } else {
                        cart.removeItem(item.get());
                    }
                });

        return Optional.of(cartRepository.save(cart));
    }
}
