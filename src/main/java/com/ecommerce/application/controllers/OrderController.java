package com.ecommerce.application.controllers;

import java.util.List;

import com.ecommerce.application.model.dto.OrderResponse;
import com.ecommerce.application.model.persistence.UserOrder;
import com.ecommerce.application.service.OrderService;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderController {
	
	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping("/submit/{username}")
	public ResponseEntity<OrderResponse> submit(@PathVariable String username) {
		var order = orderService.submit(username);
		return order.isPresent() ? ResponseEntity.ok(toOrderResponse(order.get())) : ResponseEntity.badRequest().build();
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<OrderResponse>> getOrdersForUser(@PathVariable String username) {
		var orders = orderService.findByUsername(username);
		if(orders.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(orders.get().stream().map(this::toOrderResponse).toList());
	}

	private OrderResponse toOrderResponse(UserOrder order){
		return new SpelAwareProxyProjectionFactory().createProjection(OrderResponse.class, order);
	}
}
