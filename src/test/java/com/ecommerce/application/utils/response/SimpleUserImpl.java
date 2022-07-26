package com.ecommerce.application.utils.response;

import com.ecommerce.application.model.dto.SimpleCart;
import com.ecommerce.application.model.dto.SimpleUser;

public record SimpleUserImpl(String username, Long id, SimpleCartImpl cart) implements SimpleUser {
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public SimpleCart getCart() {
        return cart;
    }
}
