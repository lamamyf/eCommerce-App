package com.ecommerce.application.controllers;

import java.util.List;

import com.ecommerce.application.model.persistence.Item;
import com.ecommerce.application.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/item")
public class ItemController {

	private final ItemService itemService;
	private static final Logger log = LoggerFactory.getLogger(ItemController.class);

	public ItemController(ItemService itemService) {
		this.itemService = itemService;
	}

	@GetMapping
	public ResponseEntity<List<Item>> getItems() {
		List<Item> items = itemService.findAll();
		log.info("getItems: {}", items.size());
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		var item = itemService.findById(id);
		if(item.isEmpty()){
			log.error("Item not found for given itemId {}", id);
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(item.get());
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		List<Item> items = itemService.findByName(name);
		if(items == null || items.isEmpty()){
			log.error("Items not found for given itemName {}", name);
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(items);
			
	}
}
