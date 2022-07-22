package com.ecommerce.application.service;

import com.ecommerce.application.model.persistence.UserOrder;
import com.ecommerce.application.model.persistence.repositories.OrderRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final UserService userService;
    private final ItemService itemService;
    private final OrderRepository orderRepository;

    private static final Logger log = LogManager.getLogger(OrderService.class);

    public OrderService(UserService userService, ItemService itemService, OrderRepository orderRepository) {
        this.userService = userService;
        this.itemService = itemService;
        this.orderRepository = orderRepository;
    }

    public Optional<UserOrder> submit(String username) {
        var user = userService.findByUsername(username);
        if(user.isEmpty()) {
            log.error("user not found for given username {}", username);
            return Optional.empty();
        }

        UserOrder order = orderRepository.save(UserOrder.createFromCart(user.get().getCart()));
        log.info("New order: {}", order.getId());

        return Optional.of(order);
    }

    public Optional<List<UserOrder>> findByUsername(String username) {
        var user = userService.findByUsername(username);
        return user.isPresent() ? Optional.of(orderRepository.findByUser(user.get())) : Optional.empty();
    }
}
