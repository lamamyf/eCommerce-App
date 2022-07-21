package com.ecommerce.application.service;

import com.ecommerce.application.model.persistence.Item;
import com.ecommerce.application.model.persistence.repositories.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }


    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }
}
