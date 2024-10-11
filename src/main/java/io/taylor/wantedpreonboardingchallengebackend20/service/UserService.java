package io.taylor.wantedpreonboardingchallengebackend20.service;

import io.taylor.wantedpreonboardingchallengebackend20.dto.request.UserJoinRequest;
import io.taylor.wantedpreonboardingchallengebackend20.dto.request.UserLoginRequest;
import io.taylor.wantedpreonboardingchallengebackend20.dto.response.UserJoinResponse;
import io.taylor.wantedpreonboardingchallengebackend20.dto.response.UserLoginResponse;
import io.taylor.wantedpreonboardingchallengebackend20.entity.User;
import io.taylor.wantedpreonboardingchallengebackend20.repository.UserRepository;
import io.taylor.wantedpreonboardingchallengebackend20.util.JwtTokenUtil;
import io.taylor.wantedpreonboardingchallengebackend20.util.PasswordUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;
    private final JwtTokenUtil jwtTokenUtil;

    public UserService(UserRepository userRepository, PasswordUtil passwordUtil, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.passwordUtil = passwordUtil;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public UserJoinResponse join(UserLoginRequest request) {
        request.setPassword(passwordUtil.encodePassword(request.getPassword()));
        User user = userRepository.save(new User(request));
        return new UserJoinResponse(user);
    }

    public UserLoginResponse login(UserJoinRequest request) {
        User user = userRepository.findUserByEmail(request.getEmail());
        if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "회원 정보가 존재하지 않습니다.");
        if (!passwordUtil.matchPassword(request.getPassword(), user.getPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "올바르지 않은 비밀번호 입니다.");

        return new UserLoginResponse(user, jwtTokenUtil.generateToken(request.getEmail()));
    }

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public Object logout(HttpHeaders headers, String str) {
        return true;
    }
}
