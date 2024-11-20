package io.taylor.api.controller.log;

import io.taylor.api.controller.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LogControllerTest extends ControllerTestSupport {

    @Test
    @DisplayName("활동 로그를 조회한다.")
    void getAllLog() throws Exception {
        // when & then
        mockMvc.perform(
                        get("/api/logs")
                ).andDo(print())
                .andExpect(jsonPath("$").isArray())
                .andExpect(status().isOk());
    }
}