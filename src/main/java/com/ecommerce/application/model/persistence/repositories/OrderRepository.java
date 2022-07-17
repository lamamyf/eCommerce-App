package com.ecommerce.application.model.persistence.repositories;

import java.util.List;

import com.ecommerce.application.model.persistence.User;
import com.ecommerce.application.model.persistence.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<UserOrder, Long> {
	List<UserOrder> findByUser(User user);
}
