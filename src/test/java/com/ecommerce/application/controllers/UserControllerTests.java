package com.ecommerce.application.controllers;

import com.ecommerce.application.utils.UserCreationResponseImpl;
import com.ecommerce.application.core.security.SecurityConfig;
import com.ecommerce.application.model.dto.CreateUserRequest;
import com.ecommerce.application.model.dto.UserCreationResponse;
import com.ecommerce.application.model.persistence.Cart;
import com.ecommerce.application.model.persistence.User;
import com.ecommerce.application.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
@AutoConfigureJsonTesters
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<CreateUserRequest> json;

    @MockBean
    private UserService userService;

    @Test
    public void createUserTest() throws Exception {
        var username = "username";
        var password = "1234567";
        var request = new CreateUserRequest(username, password, password);

        User savedUser = getUser(1L, username, password, new Cart());
        given(userService.create(username, password)).willReturn(savedUser);

        ResultActions resultActions = mockMvc.perform(post("/api/user/create")
                .content(json.write(request).getJson())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());

        String responseAsString = resultActions.andReturn().getResponse().getContentAsString();
        assertNotNull(responseAsString);

        UserCreationResponse creationResponse = new ObjectMapper().readValue(responseAsString, UserCreationResponseImpl.class);
        assertNotNull(creationResponse);
        assertEquals(username, creationResponse.getUsername());
        assertEquals(savedUser.getId(), creationResponse.getId());
    }

    private User getUser(Long id, String username, String password, Cart cart) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setCart(cart);
        if(cart != null) {
            cart.setUser(user);
        }
        return user;
    }
}
