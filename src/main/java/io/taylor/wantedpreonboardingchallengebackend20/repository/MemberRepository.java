package io.taylor.wantedpreonboardingchallengebackend20.repository;

import io.taylor.wantedpreonboardingchallengebackend20.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findMemberByEmail(String email);

    Member findMemberByEmailAndPassword(String email, String password);
}
