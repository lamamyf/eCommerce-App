package com.ecommerce.application.utils.response;

import com.ecommerce.application.model.dto.SimpleCart;
import com.ecommerce.application.model.persistence.Item;

import java.math.BigDecimal;
import java.util.List;

public record SimpleCartImpl(Long id, List<Item> items, BigDecimal total) implements SimpleCart {

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public List<Item> getItems() {
        return items;
    }

    @Override
    public BigDecimal getTotal() {
        return total;
    }
}
