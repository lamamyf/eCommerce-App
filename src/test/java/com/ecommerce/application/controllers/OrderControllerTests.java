package com.ecommerce.application.controllers;

import com.ecommerce.application.model.dto.OrderResponse;
import com.ecommerce.application.model.persistence.Cart;
import com.ecommerce.application.model.persistence.Item;
import com.ecommerce.application.model.persistence.User;
import com.ecommerce.application.model.persistence.UserOrder;
import com.ecommerce.application.service.OrderService;
import com.ecommerce.application.utils.response.OrderResponseImpl;
import com.ecommerce.application.utils.TestUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTests {

    private final static String SUBMIT_ORDER_URL        = "/api/order/submit/%s";
    private final static String GET_ORDERS_FOR_USER_URL = "/api/order/history/%s";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    @WithMockUser
    public void submitOrderTest() throws Exception {
        UserOrder order = getUserOrder();
        given(orderService.submit(order.getUser().getUsername()))
                .willReturn(Optional.of(order));

        ResultActions resultActions = TestUtils.performPost(mockMvc, SUBMIT_ORDER_URL.formatted(order.getUser().getUsername()));
        resultActions.andExpect(status().isOk());
        String json = resultActions.andReturn().getResponse().getContentAsString();
        assertNotNull(json);
        validateOrderResponse(order, new ObjectMapper().readValue(json, OrderResponseImpl.class));
    }

    @Test
    @WithMockUser
    public void submitOrderForNonexistentUserTest() throws Exception {
        ResultActions resultActions = TestUtils.performPost(mockMvc, SUBMIT_ORDER_URL.formatted("x"));
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void getOrdersForUserTest() throws Exception {
        var username = "username";
        List<UserOrder> expected = List.of(getUserOrder());

        given(orderService.findByUsername(username)).willReturn(Optional.of(expected));
        ResultActions resultActions = TestUtils.performGet(mockMvc, GET_ORDERS_FOR_USER_URL.formatted(username));
        resultActions.andExpect(status().isOk());
        String json = resultActions.andReturn().getResponse().getContentAsString();
        assertNotNull(json);
        List<OrderResponseImpl> actual = new ObjectMapper().readValue(json, new TypeReference<List<OrderResponseImpl>>() {});
        assertNotNull(actual);
        assertEquals(actual.size(), expected.size());

        for (int i = 0;  i < actual.size(); i++){
            validateOrderResponse(expected.get(i), actual.get(i));
        }
    }

    private UserOrder getUserOrder(){
        Cart cart = new Cart();
        cart.setId(2L);
        cart.addItem(new Item(1L, "Round Widget", BigDecimal.valueOf(3.0), "A widget that is round"));
        cart.setUser(new User(3L, "username", cart, "12345"));

        return UserOrder.createFromCart(cart);
    }

    private void validateOrderResponse(UserOrder actual, OrderResponse expected) {
        assertNotNull(expected);
        assertEquals(actual.getItems(), expected.getItems());
        assertEquals(actual.getTotal(), expected.getTotal());
        assertEquals(actual.getUser().getUsername(), expected.getUser().getUsername());
    }
}
