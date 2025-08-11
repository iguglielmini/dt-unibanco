package br.com.itau.challenge.balance.health;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HealthController.class)
class HealthControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private HealthCheckService health;

    @Test
    void readyz_up() throws Exception {
        Mockito.when(health.checkDb()).thenReturn(Map.of("status","UP"));
        Mockito.when(health.checkSqs()).thenReturn(Map.of("status","UP"));

        mvc.perform(get("/readyz").accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.status", is("UP")));
    }

    @Test
    void readyz_down() throws Exception {
        Mockito.when(health.checkDb()).thenReturn(Map.of("status","DOWN"));
        Mockito.when(health.checkSqs()).thenReturn(Map.of("status","UP"));

        mvc.perform(get("/readyz").accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isServiceUnavailable())
           .andExpect(jsonPath("$.status", is("DOWN")));
    }
}
