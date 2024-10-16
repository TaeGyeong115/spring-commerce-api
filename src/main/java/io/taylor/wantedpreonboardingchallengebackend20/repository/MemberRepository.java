package io.taylor.wantedpreonboardingchallengebackend20.repository;

import io.taylor.wantedpreonboardingchallengebackend20.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findMemberByEmail(String email);
}
