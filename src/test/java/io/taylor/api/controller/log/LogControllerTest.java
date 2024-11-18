package io.taylor.api.controller.log;

import io.taylor.api.controller.ControllerTestSupport;
import io.taylor.api.controller.log.response.LogResponse;
import io.taylor.domain.log.ActionType;
import io.taylor.domain.log.TargetType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LogControllerTest extends ControllerTestSupport {

    @Test
    @DisplayName("활동 로그를 조회한다.")
    void getAllLog() throws Exception {
        // given
        List<LogResponse> response = List.of(createLogResponse(1L, 1L, TargetType.PRODUCT, ActionType.COMPLETE));
        given(logService.getActivityLogs(createAuthMember())).willReturn(response);

        // when & then
        mockMvc.perform(
                        get("/api/logs")
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.get(0).id()))
                .andExpect(jsonPath("$[0].targetId").value(response.get(0).targetId()))
                .andExpect(jsonPath("$[0].targetType").value(response.get(0).targetType()))
                .andExpect(jsonPath("$[0].actionType").value(response.get(0).actionType()))
                .andExpect(jsonPath("$[0].modifiedDateTime").hasJsonPath())
                .andExpect(jsonPath("$[0].createdDateTime").hasJsonPath());
    }

    private LogResponse createLogResponse(Long id, Long targetId, TargetType targetType, ActionType actionType) {
        return LogResponse.builder()
                .id(id)
                .targetId(targetId)
                .targetType(targetType.name())
                .actionType(actionType.name())
                .build();
    }
}