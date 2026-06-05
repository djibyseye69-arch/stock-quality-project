package com.qualite.stock.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(StockController.class)
class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testRisk_Zero_ReturnsHigh() throws Exception {
        mockMvc.perform(get("/api/v1/risk-index").param("lvl", "0"))
               .andExpect(status().isOk())
               .andExpect(content().string("High"));
    }

    @Test
    void testRisk_OddLow_ReturnsLowA() throws Exception {
        mockMvc.perform(get("/api/v1/risk-index").param("lvl", "3"))
               .andExpect(status().isOk())
               .andExpect(content().string("Low-A"));
    }

    @Test
    void testRisk_EvenLow_ReturnsLowB() throws Exception {
        mockMvc.perform(get("/api/v1/risk-index").param("lvl", "4"))
               .andExpect(status().isOk())
               .andExpect(content().string("Low-B"));
    }

    @Test
    void testRisk_Medium_ReturnsMedium() throws Exception {
        mockMvc.perform(get("/api/v1/risk-index").param("lvl", "25"))
               .andExpect(status().isOk())
               .andExpect(content().string("Medium"));
    }

    @Test
    void testRisk_High_ReturnsHigh() throws Exception {
        mockMvc.perform(get("/api/v1/risk-index").param("lvl", "100"))
               .andExpect(status().isOk())
               .andExpect(content().string("High"));
    }

    @Test
    void testHeavyProcessing_DefaultPage() throws Exception {
        mockMvc.perform(get("/api/v1/process-data"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.page").value(0))
               .andExpect(jsonPath("$.size").value(100))
               .andExpect(jsonPath("$.total").value(10000));
    }

    @Test
    void testHeavyProcessing_CustomPage() throws Exception {
        mockMvc.perform(get("/api/v1/process-data")
               .param("page", "1")
               .param("size", "50"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.page").value(1));
    }

}