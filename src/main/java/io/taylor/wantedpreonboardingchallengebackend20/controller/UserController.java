package io.taylor.wantedpreonboardingchallengebackend20.controller;

import io.taylor.wantedpreonboardingchallengebackend20.dto.request.JoinRequestDto;
import io.taylor.wantedpreonboardingchallengebackend20.dto.request.LoginRequestDto;
import io.taylor.wantedpreonboardingchallengebackend20.dto.response.JoinResponseDto;
import io.taylor.wantedpreonboardingchallengebackend20.dto.response.LoginResponseDto;
import io.taylor.wantedpreonboardingchallengebackend20.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/join")
    public ResponseEntity<JoinResponseDto> join(@RequestBody JoinRequestDto request) {
        JoinResponseDto response = userService.join(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequestDto request) {
        LoginResponseDto response = userService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(@RequestHeader HttpHeaders header, String str) {
        Object response = userService.logout(header, str);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
