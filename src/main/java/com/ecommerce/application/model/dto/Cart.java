package com.ecommerce.application.model.dto;

import com.ecommerce.application.model.persistence.Item;

import java.math.BigDecimal;
import java.util.List;

public interface Cart {
    Long getId();
    List<Item> getItems();
    BigDecimal getTotal();
}
