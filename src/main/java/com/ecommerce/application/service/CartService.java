package com.ecommerce.application.service;

import com.ecommerce.application.model.persistence.Cart;
import com.ecommerce.application.model.persistence.repositories.CartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.IntStream;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserService userService;
    private final ItemService itemService;

    private static final Logger log = LoggerFactory.getLogger(CartService.class);

    public CartService(CartRepository cartRepository, UserService userService, ItemService itemService) {
        this.cartRepository = cartRepository;
        this.userService = userService;
        this.itemService = itemService;
    }


    public Optional<Cart> modifyCart(String username, Long itemId, Integer quantity, boolean addItems) {
        var user = userService.findByUsername(username);
        if(user.isEmpty()) {
            log.error("User not found for given username {}", username);
            return Optional.empty();
        }

        var item = itemService.findById(itemId);
        if(item.isEmpty()) {
            log.error("Item not found for given itemId {}", itemId);
            return Optional.empty();
        }

        Cart cart = user.get().getCart();
        IntStream.range(0, quantity)
                .forEach(i -> {
                    if (addItems) {
                        cart.addItem(item.get());
                        log.info("Item {} added to cart {}", item.get().getId(), cart.getId());
                    } else {
                        cart.removeItem(item.get());
                        log.info("Item {} removed from cart {}", item.get().getId(), cart.getId());
                    }
                });

        return Optional.of(cartRepository.save(cart));
    }
}
