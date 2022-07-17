package com.ecommerce.application.model.persistence.repositories;

import java.util.List;

import com.ecommerce.application.model.persistence.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
	List<Item> findByName(String name);
}
