package com.ecommerce.application.model.dto;

public record ModifyCartRequest (String username, Long itemId, Integer quantity) {
}
