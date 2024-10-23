package io.taylor.wantedpreonboardingchallengebackend20.api.controller.member;

import io.taylor.wantedpreonboardingchallengebackend20.api.controller.member.request.AuthenticatedMember;
import io.taylor.wantedpreonboardingchallengebackend20.api.controller.member.request.MemberJoinRequest;
import io.taylor.wantedpreonboardingchallengebackend20.api.controller.member.request.MemberLoginRequest;
import io.taylor.wantedpreonboardingchallengebackend20.api.controller.member.response.MemberLoginResponse;
import io.taylor.wantedpreonboardingchallengebackend20.api.service.member.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<Void> join(@Valid @RequestBody MemberJoinRequest request) {
        memberService.join(request.toServiceRequest());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponse> login(@Valid @RequestBody MemberLoginRequest request) {
        MemberLoginResponse response = memberService.login(request.toServiceRequest());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(AuthenticatedMember authenticatedMember, @RequestBody String requestBody) {
        memberService.logout();
        return ResponseEntity.ok().build();
    }
}

