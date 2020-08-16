package com.mkcode.simplebanking;

import com.mkcode.simplebanking.repositories.OperationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class SimplebankingApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OperationRepository operationRepository;

    @Test
    @WithMockUser(value = "john")
    void verifyAccountsEndpointReturnListOfAccountsNo() throws Exception {
        mockMvc.perform(get("/accounts")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].accountNo").value("LT000000000000000001"))
                .andExpect(jsonPath("$.[1].accountNo").value("LT000000000000000002"))
                .andExpect(jsonPath("$.[2].accountNo").value("LT000000000000000003"));
    }

    @Test
    @WithMockUser(value = "john")
    void verifyAccountsBalanceEndpoint() throws Exception {
        mockMvc.perform(get("/accounts/LT000000000000000002/balance")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }

    @Test
    @WithMockUser(value = "john")
    void verifyPostOperationEndpointSavesOperationToDb() throws Exception {
        assertThat(operationRepository.getAccountBalance(3)).isEmpty();

        mockMvc.perform(post("/accounts/LT000000000000000003/operations")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"operationType\": \"DEPOSIT\",\n" +
                        "  \"amount\": 100\n" +
                        "}"))
                .andExpect(status().isAccepted());

        assertThat(operationRepository.getAccountBalance(3))
                .isPresent()
                .hasValue(BigDecimal.valueOf(100));
    }
}