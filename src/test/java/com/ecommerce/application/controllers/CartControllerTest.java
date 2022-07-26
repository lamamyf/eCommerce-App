package com.ecommerce.application.controllers;

import com.ecommerce.application.model.dto.ModifyCartRequest;
import com.ecommerce.application.model.persistence.Cart;
import com.ecommerce.application.model.persistence.Item;
import com.ecommerce.application.model.persistence.User;
import com.ecommerce.application.service.CartService;
import com.ecommerce.application.utils.TestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
@AutoConfigureJsonTesters
public class CartControllerTest {

    private final static String ADD_TO_CART_URL = "/api/cart/addToCart";
    private final static String REMOVE_FROM_CART_URL = "/api/cart/removeFromCart";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<ModifyCartRequest> jacksonTester;

    @MockBean
    private CartService cartService;

    @Test
    @WithMockUser
    public void addItemTest() throws Exception {
        var request = new ModifyCartRequest("username", 1L, 1);
        Cart expectedCart = getCart();

        given(cartService.modifyCart(request.username(), request.itemId(), request.quantity(), true))
                .willReturn(Optional.of(expectedCart));

        ResultActions resultActions = TestUtils.performPost(mockMvc, ADD_TO_CART_URL, jacksonTester.write(request).getJson());
        resultActions.andExpect(status().isOk());
        validateCartResponse(expectedCart, resultActions.andReturn().getResponse().getContentAsString());
    }

    @Test
    @WithMockUser
    public void addNonexistentItemTest() throws Exception {
        var request = new ModifyCartRequest("username", 10L, 1);

        given(cartService.modifyCart(request.username(), request.itemId(), request.quantity(), true))
                .willReturn(Optional.empty());

        ResultActions resultActions = TestUtils.performPost(mockMvc, ADD_TO_CART_URL, jacksonTester.write(request).getJson());
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void addItemForNonexistentUserTest() throws Exception {
        var request = new ModifyCartRequest("username-x", 1L, 1);

        given(cartService.modifyCart(request.username(), request.itemId(), request.quantity(), true))
                .willReturn(Optional.empty());

        ResultActions resultActions = TestUtils.performPost(mockMvc, ADD_TO_CART_URL, jacksonTester.write(request).getJson());
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void removeItemTest() throws Exception {
        var request = new ModifyCartRequest("username", 1L, 1);
        Cart expectedCart = getCart();
        expectedCart.removeItem(expectedCart.getItems().get(0));

        given(cartService.modifyCart(request.username(), request.itemId(), request.quantity(), false))
                .willReturn(Optional.of(expectedCart));

        ResultActions resultActions = TestUtils.performPost(mockMvc, REMOVE_FROM_CART_URL, jacksonTester.write(request).getJson());
        resultActions.andExpect(status().isOk());
        validateCartResponse(expectedCart, resultActions.andReturn().getResponse().getContentAsString());
    }


    private Cart getCart(){
        Cart cart = new Cart();
        cart.setId(2L);
        cart.addItem(new Item(1L, "Round Widget", BigDecimal.valueOf(3.0), "A widget that is round"));
        User user = new User(3L, "username", cart, "12345");
        cart.setUser(user);

        return cart;
    }

    private void validateCartResponse(Cart expected, String json) throws JsonProcessingException {
        assertNotNull(json);
        var actual = new ObjectMapper().readValue(json, Cart.class);
        assertNotNull(actual);
        assertEquals(actual.getId(), expected.getId());
        assertEquals(actual.getTotal(), expected.getTotal());
        assertEquals(actual.getItems(), expected.getItems());
    }
}
