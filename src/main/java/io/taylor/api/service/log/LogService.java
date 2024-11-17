package io.taylor.api.service.log;

import io.taylor.api.controller.log.response.LogResponse;
import io.taylor.api.controller.member.request.AuthenticatedMember;
import io.taylor.domain.log.ActionType;
import io.taylor.domain.log.Log;
import io.taylor.domain.log.LogRepository;
import io.taylor.domain.log.TargetType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    public List<LogResponse> getActivityLogs(AuthenticatedMember authenticatedMember) {
        List<Log> logList = logRepository.findByMemberId(authenticatedMember.memberId());
        return logList.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public void saveLog(ActionType actionType, TargetType targetType, long memberId, long targetId) {
        Log log = Log.builder()
                .memberId(memberId)
                .targetId(targetId)
                .actionType(actionType)
                .targetType(targetType)
                .build();
        logRepository.save(log);
    }

    private LogResponse convertToResponse(Log log) {
        return LogResponse.builder()
                .id(log.getId())
                .targetId(log.getTargetId())
                .targetType(log.getTargetType().getName())
                .actionType(log.getActionType().getName())
                .createdDateTime(log.getCreatedDateTime())
                .modifiedDateTime(log.getModifiedDateTime())
                .build();
    }
}
