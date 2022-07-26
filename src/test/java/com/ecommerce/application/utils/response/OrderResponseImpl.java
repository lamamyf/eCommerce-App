package com.ecommerce.application.utils.response;

import com.ecommerce.application.model.dto.OrderResponse;
import com.ecommerce.application.model.dto.SimpleUser;
import com.ecommerce.application.model.persistence.Item;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponseImpl(Long id, List<Item> items, SimpleUserImpl user, BigDecimal total) implements OrderResponse {

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public List<Item> getItems() {
        return items;
    }

    @Override
    public SimpleUser getUser() {
        return user;
    }

    @Override
    public BigDecimal getTotal() {
        return total;
    }
}
