package io.taylor.wantedpreonboardingchallengebackend20.service;

import io.taylor.wantedpreonboardingchallengebackend20.dto.request.MemberJoinRequest;
import io.taylor.wantedpreonboardingchallengebackend20.entity.Member;
import io.taylor.wantedpreonboardingchallengebackend20.repository.MemberRepository;
import io.taylor.wantedpreonboardingchallengebackend20.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class MemberServiceTest {

    @Mock
    PasswordUtil passwordUtil;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("[Success] 회원가입 테스트")
    void joinTest() {
        // given
        MemberJoinRequest request = new MemberJoinRequest("test", "test", "test@test.com", "test");
        Member member = new Member(request.email(), request.nickName(), request.email(), passwordUtil.encodePassword(request.password()));

        when(memberRepository.save(any(Member.class))).thenReturn(member);
        when(memberRepository.findMemberByEmail(request.email())).thenReturn(member);

        // when
        memberService.join(request);
        Member foundMember = memberService.getMemberByEmail(request.email());

        // then
        assertThat(foundMember).isNotNull();
        assertThat(foundMember.getEmail()).isEqualTo(request.email());
        assertThat(foundMember.getName()).isEqualTo(request.name());
        assertThat(foundMember.getNickName()).isEqualTo(request.name());
    }
}