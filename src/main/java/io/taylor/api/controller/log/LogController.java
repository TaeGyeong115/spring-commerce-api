package io.taylor.api.controller.log;

import io.taylor.api.controller.log.response.LogResponse;
import io.taylor.api.controller.member.request.AuthenticatedMember;
import io.taylor.api.service.log.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/logs")
public class LogController {

    private final LogService logService;

    @GetMapping()
    public ResponseEntity<List<LogResponse>> getActivityLogs(AuthenticatedMember member) {
        List<LogResponse> response = logService.getActivityLogs(member.memberId());
        return ResponseEntity.ok(response);
    }
}
