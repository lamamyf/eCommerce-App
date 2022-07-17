package com.ecommerce.application.model.persistence.repositories;

import com.ecommerce.application.model.persistence.Cart;
import com.ecommerce.application.model.persistence.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
	Cart findByUser(User user);
}
