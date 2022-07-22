package com.ecommerce.application.model.dto;

public record CreateUserRequest (String username, String password, String confirmPassword){
}
