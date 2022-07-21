package com.ecommerce.application.controllers;

import com.ecommerce.application.model.dto.SimpleUser;
import com.ecommerce.application.model.dto.UserCreationResponse;
import com.ecommerce.application.model.persistence.User;
import com.ecommerce.application.model.dto.CreateUserRequest;
import com.ecommerce.application.service.UserService;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private final UserService userService;
	private final SpelAwareProxyProjectionFactory projectionFactory;

	public UserController(UserService userService, SpelAwareProxyProjectionFactory projectionFactory) {
		this.userService = userService;
		this.projectionFactory = projectionFactory;
	}

	@GetMapping("id/{id}")
	public ResponseEntity<SimpleUser> findById(@PathVariable Long id) {
		Optional<User> user = userService.findById(id);
		return user.isPresent() ? ResponseEntity.ok(toSimpleUser(user.get())) :  ResponseEntity.notFound().build();
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<SimpleUser> findByUserName(@PathVariable String username) {
		Optional<User> user = userService.findByUsername(username);
		return user.isPresent() ? ResponseEntity.ok(toSimpleUser(user.get())) : ResponseEntity.notFound().build();
	}

	@PostMapping("/create")
	public ResponseEntity<UserCreationResponse> createUser(@RequestBody CreateUserRequest request) {
		if(request.password().length() < 7 ){
			return ResponseEntity.badRequest().build();
		}
		User user = userService.create(request.username(), request.password());
		return ResponseEntity.ok(toUserResponse(user));
	}

	private SimpleUser toSimpleUser(User user){
		return projectionFactory.createProjection(SimpleUser.class, user);
	}

	private UserCreationResponse toUserResponse(User user){
		return projectionFactory.createProjection(UserCreationResponse.class, user);
	}
}
