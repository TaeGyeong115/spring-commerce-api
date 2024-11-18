package io.taylor.domain.member;

import io.taylor.api.service.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class MemberRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("이메일로 회원정보를 조회한다.")
    void findMemberByEmail() {
        // given
        Member member = createMember("member1@test.com", "password", "member1", "member1");
        memberRepository.save(member);

        // when
        Member findMember = memberRepository.findMemberByEmail(member.getEmail());

        // then
        assertThat(findMember)
                .extracting("email", "name")
                .contains("member1", "member1@test.com");
    }

    private Member createMember(String email, String password, String name, String nickName) {
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .nickName(nickName)
                .build();
    }

}