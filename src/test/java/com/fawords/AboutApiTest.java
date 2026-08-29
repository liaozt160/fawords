package com.fawords;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AboutApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void aboutApiReturnsCorrectContent() throws Exception {
        mockMvc.perform(get("/api/about"))
                .andExpect(status().isOk())
                .andExpect(content().string("test about"));
    }

    @Test
    void helloApiReturnsCorrectContent() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World! This is Spring Boot 4.1.0 running on Java 21 inside Fawords (fawords.com)."));
    }
}