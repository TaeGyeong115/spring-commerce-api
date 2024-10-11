package io.taylor.wantedpreonboardingchallengebackend20.controller;

import io.taylor.wantedpreonboardingchallengebackend20.dto.request.AuthenticatedMember;
import io.taylor.wantedpreonboardingchallengebackend20.dto.request.MemberJoinRequest;
import io.taylor.wantedpreonboardingchallengebackend20.dto.request.MemberLoginRequest;
import io.taylor.wantedpreonboardingchallengebackend20.dto.response.MemberJoinResponse;
import io.taylor.wantedpreonboardingchallengebackend20.dto.response.MemberLoginResponse;
import io.taylor.wantedpreonboardingchallengebackend20.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService MemberService;

    public MemberController(MemberService MemberService) {
        this.MemberService = MemberService;
    }

    @PostMapping("/join")
    public ResponseEntity<MemberJoinResponse> join(@RequestBody MemberJoinRequest request) {
        MemberJoinResponse response = MemberService.join(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody MemberLoginRequest request) {
        MemberLoginResponse response = MemberService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(AuthenticatedMember authenticatedMember, @RequestBody String requestBody) {
        MemberService.logout();
        return ResponseEntity.noContent().build();
    }
}
