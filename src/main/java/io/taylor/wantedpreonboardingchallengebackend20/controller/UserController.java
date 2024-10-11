package io.taylor.wantedpreonboardingchallengebackend20.controller;

import io.taylor.wantedpreonboardingchallengebackend20.dto.request.UserLoginRequest;
import io.taylor.wantedpreonboardingchallengebackend20.dto.request.UserJoinRequest;
import io.taylor.wantedpreonboardingchallengebackend20.dto.response.UserJoinResponse;
import io.taylor.wantedpreonboardingchallengebackend20.dto.response.UserLoginResponse;
import io.taylor.wantedpreonboardingchallengebackend20.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/join")
    public ResponseEntity<UserJoinResponse> join(@RequestBody UserLoginRequest request) {
        UserJoinResponse response = userService.join(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserJoinRequest request) {
        UserLoginResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(@RequestHeader HttpHeaders header, @RequestBody String requestBody) {
        userService.logout(header, requestBody);
        return ResponseEntity.noContent().build();
    }
}
