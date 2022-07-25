package com.ecommerce.application.controllers;

import com.ecommerce.application.utils.TestUtils;
import com.ecommerce.application.utils.UserCreationResponseImpl;
import com.ecommerce.application.core.security.SecurityConfig;
import com.ecommerce.application.model.dto.CreateUserRequest;
import com.ecommerce.application.model.dto.UserCreationResponse;
import com.ecommerce.application.model.persistence.Cart;
import com.ecommerce.application.model.persistence.User;
import com.ecommerce.application.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
@AutoConfigureJsonTesters
public class UserControllerTests {

    private static final String CREATE_USER_URL = "/api/user/create";
    private static final String FIND_BY_USERNAME_URL = "/api/user/%s";
    private static final String FIND_BY_ID_URL = "/api/user/id/%d";

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

        ResultActions resultActions = TestUtils.performPost(mockMvc, CREATE_USER_URL, json.write(request).getJson());

        resultActions.andExpect(status().isOk());

        String responseAsString = resultActions.andReturn().getResponse().getContentAsString();
        assertNotNull(responseAsString);

        UserCreationResponse creationResponse = new ObjectMapper().readValue(responseAsString, UserCreationResponseImpl.class);
        assertNotNull(creationResponse);
        assertEquals(username, creationResponse.getUsername());
        assertEquals(savedUser.getId(), creationResponse.getId());
    }

    @Test
    public void createUserWithWeakPasswordTest() throws Exception {
        var username = "username";
        var password = "123";
        var request = new CreateUserRequest(username, password, password);

        ResultActions resultActions = TestUtils.performPost(mockMvc, CREATE_USER_URL, json.write(request).getJson());

        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void findByUsernameTest() throws Exception {
        var username = "lama";
        User user = getUser(1L, username, "1234", new Cart());
        given(userService.findByUsername(username)).willReturn(Optional.of(user));

        ResultActions resultActions = TestUtils.performGet(mockMvc, FIND_BY_USERNAME_URL.formatted(username));

        resultActions.andExpect(status().isOk());

        validateUserResponse(user.getId(), username, resultActions.andReturn().getResponse().getContentAsString());
    }

    @Test
    @WithMockUser
    public void findByNonexistentUsernameTest() throws Exception {
        given(userService.findByUsername("username")).willReturn(Optional.empty());

        ResultActions resultActions = TestUtils.performGet(mockMvc, FIND_BY_USERNAME_URL.formatted("username"));

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void findByIdTest() throws Exception {
        var id = 1L;
        User user = getUser(1L, "username", "1234", new Cart());
        given(userService.findById(id)).willReturn(Optional.of(user));

        ResultActions resultActions = TestUtils.performGet(mockMvc, FIND_BY_ID_URL.formatted(id));

        resultActions.andExpect(status().isOk());

        validateUserResponse(id, user.getUsername(), resultActions.andReturn().getResponse().getContentAsString());
    }

    @Test
    @WithMockUser
    public void findByNonexistentIdTest() throws Exception {
        given(userService.findById(10L)).willReturn(Optional.empty());

        ResultActions resultActions = TestUtils.performGet(mockMvc, FIND_BY_ID_URL.formatted(10L));
        resultActions.andExpect(status().isNotFound());
    }

    private void validateUserResponse(Long id, String username, String json) throws JsonProcessingException {
        assertNotNull(json);
        User retrievedUser = new ObjectMapper().readValue(json, User.class);
        assertNotNull(retrievedUser);
        assertEquals(username, retrievedUser.getUsername());
        assertEquals(retrievedUser.getId(), id);
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
