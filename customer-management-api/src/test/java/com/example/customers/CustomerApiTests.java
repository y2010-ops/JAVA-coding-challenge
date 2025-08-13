package com.example.customers;

import com.example.customers.dto.CustomerRequest;
import com.example.customers.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerApiTests {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired CustomerRepository repo;

    @Test
    void create_and_get_by_id() throws Exception {
        CustomerRequest req = new CustomerRequest();
        req.setName("John");
        req.setEmail("john@example.com");
        req.setAnnualSpend(null);
        String body = objectMapper.writeValueAsString(req);

        String created = mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();

        String id = objectMapper.readTree(created).get("id").asText();

        mockMvc.perform(get("/customers/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.tier").value("Silver"));
    }

    @Test
    void validation_on_email() throws Exception {
        CustomerRequest req = new CustomerRequest();
        req.setName("Bad Email");
        req.setEmail("not-an-email");
        String body = objectMapper.writeValueAsString(req);

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void tier_gold_and_platinum() throws Exception {
        // Gold
        CustomerRequest gold = new CustomerRequest();
        gold.setName("Goldy");
        gold.setEmail("gold@example.com");
        gold.setAnnualSpend(new java.math.BigDecimal("5000"));
        gold.setLastPurchaseDate(OffsetDateTime.now().minusMonths(2));
        String gbody = objectMapper.writeValueAsString(gold);
        String gresp = mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON).content(gbody))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        String gid = objectMapper.readTree(gresp).get("id").asText();
        mockMvc.perform(get("/customers/" + gid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tier").value("Gold"));

        // Platinum
        CustomerRequest plat = new CustomerRequest();
        plat.setName("Platy");
        plat.setEmail("plat@example.com");
        plat.setAnnualSpend(new java.math.BigDecimal("15000"));
        plat.setLastPurchaseDate(OffsetDateTime.now().minusMonths(3));
        String pbody = objectMapper.writeValueAsString(plat);
        String presp = mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON).content(pbody))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        String pid = objectMapper.readTree(presp).get("id").asText();
        mockMvc.perform(get("/customers/" + pid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tier").value("Platinum"));
    }

    @Test
    void update_and_delete() throws Exception {
        CustomerRequest req = new CustomerRequest();
        req.setName("To Update");
        req.setEmail("upd@example.com");
        String body = objectMapper.writeValueAsString(req);

        String created = mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        String id = objectMapper.readTree(created).get("id").asText();

        req.setName("Updated");
        String ubody = objectMapper.writeValueAsString(req);

        mockMvc.perform(put("/customers/" + id)
                .contentType(MediaType.APPLICATION_JSON).content(ubody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));

        mockMvc.perform(delete("/customers/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/customers/" + id))
                .andExpect(status().isNotFound());
    }
}
