package io.taylor.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findMemberByEmail(String email);

    boolean existsByEmail(String email);
}
