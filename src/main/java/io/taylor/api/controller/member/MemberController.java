package io.taylor.api.controller.member;

import io.taylor.api.controller.member.request.AuthenticatedMember;
import io.taylor.api.controller.member.request.MemberJoinRequest;
import io.taylor.api.controller.member.request.MemberLoginRequest;
import io.taylor.api.controller.member.response.MemberLoginResponse;
import io.taylor.api.service.member.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(AuthenticatedMember authenticatedMember) {
        memberService.logout();
        return ResponseEntity.ok().build();
    }
}

