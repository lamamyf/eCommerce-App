package com.ecommerce.application.model.dto;

import com.ecommerce.application.model.persistence.Item;

import java.math.BigDecimal;
import java.util.List;

public interface OrderResponse {
    Long getId();
    List<Item> getItems();
    SimpleUser getUser();
    BigDecimal getTotal();
}
