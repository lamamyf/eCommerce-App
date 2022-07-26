package com.ecommerce.application.utils.response;

import com.ecommerce.application.model.dto.UserCreationResponse;

public record UserCreationResponseImpl(Long id, String username) implements UserCreationResponse {
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
