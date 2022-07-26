package com.ecommerce.application.controllers;

import com.ecommerce.application.model.dto.SimpleCart;
import com.ecommerce.application.model.persistence.Cart;
import com.ecommerce.application.model.dto.ModifyCartRequest;
import com.ecommerce.application.service.CartService;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {
	
	private final CartService cartService;

	public CartController(CartService cartService) {
		this.cartService = cartService;
	}

	@PostMapping("/addToCart")
	public ResponseEntity<SimpleCart> addToCart(@RequestBody ModifyCartRequest request) {
		var cart = cartService.modifyCart(request.username(), request.itemId(), request.quantity(), true);
		return cart.isPresent() ? ResponseEntity.ok(toSimpleCart(cart.get())) : ResponseEntity.badRequest().build();
	}
	
	@PostMapping("/removeFromCart")
	public ResponseEntity<SimpleCart> removeFromCart(@RequestBody ModifyCartRequest request) {
		var cart = cartService.modifyCart(request.username(), request.itemId(), request.quantity(), false);
		return cart.isPresent() ? ResponseEntity.ok(toSimpleCart(cart.get())) : ResponseEntity.badRequest().build();
	}

	private SimpleCart toSimpleCart(Cart cart){
		return new SpelAwareProxyProjectionFactory().createProjection(SimpleCart.class, cart);
	}
}
