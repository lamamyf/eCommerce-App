package com.ecommerce.application.controllers;

import com.ecommerce.application.model.persistence.Item;
import com.ecommerce.application.service.ItemService;
import com.ecommerce.application.utils.TestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
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

@WebMvcTest(ItemController.class)
public class ItemControllerTest {

    private static final String FIND_ITEMS_URL = "/api/item";
    private static final String FIND_BY_ID_URL = "/api/item/%d";
    private static final String FIND_BY_NAME_URL = "/api/item/name/%s";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Test
    @WithMockUser
    public void findAllItemsTest() throws Exception {
        var items = List.of(getItem(1L));
        given(itemService.findAll()).willReturn(items);

        ResultActions resultActions = TestUtils.performGet(mockMvc, FIND_ITEMS_URL);

        resultActions.andExpect(status().isOk());
        validateItems(items, resultActions.andReturn().getResponse().getContentAsString());
    }

    @Test
    @WithMockUser
    public void findByIdTest() throws Exception {
        var id = 1L;
        var item = getItem(id);
        given(itemService.findById(id)).willReturn(Optional.of(item));

        String response = TestUtils.performGet(mockMvc, FIND_BY_ID_URL.formatted(id))
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString();

        assertNotNull(response);

        var retrievedItem = new ObjectMapper().readValue(response, Item.class);
        assertEquals(id, retrievedItem.getId());
        assertEquals(item.getName(), retrievedItem.getName());
    }

    @Test
    @WithMockUser
    public void findByNonexistentIdTest() throws Exception {
        given(itemService.findById(10L)).willReturn(Optional.empty());

        ResultActions resultActions = TestUtils.performGet(mockMvc, FIND_BY_ID_URL.formatted(10L));
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void findByNameTest() throws Exception {
        var name = "Round Widget";
        var items = List.of(getItem(1L));

        given(itemService.findByName(name)).willReturn(items);

        ResultActions resultActions = TestUtils.performGet(mockMvc, FIND_BY_NAME_URL.formatted(name));

        resultActions.andExpect(status().isOk());
        validateItems(items, resultActions.andReturn().getResponse().getContentAsString());
    }

    private void validateItems(List<Item> expected, String json) throws JsonProcessingException {
        assertNotNull(json);
        var actual = new ObjectMapper().readValue(json, new TypeReference<List<Item>>() {});
        assertEquals(actual, expected);
    }

    private Item getItem(Long id){
        return new Item(id, "Round Widget", BigDecimal.valueOf(3.0), "A widget that is round");
    }
}
