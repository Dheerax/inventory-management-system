package com.shop.inventory;

import com.shop.inventory.model.Product;
import com.shop.inventory.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ProductRepository repo;

    @BeforeEach
    void setup() {
        repo.deleteAll();
        repo.save(new Product("P001", "Milk 1L", 30.0, 20));
        repo.save(new Product("P002", "Bread", 25.0, 3));
    }

    @Test
    void listsAllProducts() throws Exception {
        mvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].lowStock").value(true));
    }

    @Test
    void addsProduct() throws Exception {
        mvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":\"P003\",\"name\":\"Eggs\",\"price\":6,\"quantity\":30}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Eggs"));
    }

    @Test
    void rejectsDuplicateId() throws Exception {
        mvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":\"P001\",\"name\":\"Other\",\"price\":1,\"quantity\":1}"))
                .andExpect(status().isConflict());
    }

    @Test
    void rejectsNegativePrice() throws Exception {
        mvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":\"P009\",\"name\":\"Bad\",\"price\":-5,\"quantity\":1}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Price cannot be negative"));
    }

    @Test
    void updatesStock() throws Exception {
        mvc.perform(put("/api/products/P002").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\":15}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(15))
                .andExpect(jsonPath("$.lowStock").value(false));
    }

    @Test
    void rejectsNegativeQuantityOnUpdate() throws Exception {
        mvc.perform(put("/api/products/P001").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\":-1}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void searchesByNameOrId() throws Exception {
        mvc.perform(get("/api/products/search").param("q", "brea"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].productId").value("P002"));
        mvc.perform(get("/api/products/search").param("q", "p001"))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void listsLowStock() throws Exception {
        mvc.perform(get("/api/products/low-stock"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].productId").value("P002"));
    }

    @Test
    void returns404ForUnknownProduct() throws Exception {
        mvc.perform(get("/api/products/NOPE")).andExpect(status().isNotFound());
        mvc.perform(delete("/api/products/NOPE")).andExpect(status().isNotFound());
    }

    @Test
    void deletesProduct() throws Exception {
        mvc.perform(delete("/api/products/P001")).andExpect(status().isNoContent());
        mvc.perform(get("/api/products/P001")).andExpect(status().isNotFound());
    }
}
