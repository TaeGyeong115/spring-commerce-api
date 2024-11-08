package io.taylor.api.service.member.request;

import lombok.Builder;

@Builder
public record MemberLoingServiceRequest(String email, String password) {
}
