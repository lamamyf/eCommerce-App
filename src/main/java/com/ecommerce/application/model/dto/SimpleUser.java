package com.ecommerce.application.model.dto;

public interface SimpleUser {
    Long getId();
    String getUsername();
    SimpleCart getCart();
}
